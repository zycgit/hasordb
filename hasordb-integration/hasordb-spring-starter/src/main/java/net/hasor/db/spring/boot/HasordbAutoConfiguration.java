package net.hasor.db.spring.boot;
import net.hasor.cobble.logging.Logger;
import net.hasor.cobble.logging.LoggerFactory;
import net.hasor.db.dal.dynamic.rule.RuleRegistry;
import net.hasor.db.dal.repository.DalMapper;
import net.hasor.db.dal.repository.DalRegistry;
import net.hasor.db.dal.session.DalSession;
import net.hasor.db.lambda.LambdaTemplate;
import net.hasor.db.mapping.resolve.MappingOptions;
import net.hasor.db.spring.mapper.MapperFileConfigurer;
import net.hasor.db.spring.mapper.MapperScannerConfigurer;
import net.hasor.db.types.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@ConditionalOnClass({ DalSession.class, DalRegistry.class })
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(HasordbProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class HasordbAutoConfiguration implements BeanClassLoaderAware, InitializingBean {
    private static final Logger            logger = LoggerFactory.getLogger(HasordbAutoConfiguration.class);
    private              ClassLoader       classLoader;
    private final        HasordbProperties properties;
    //private final Interceptor[]     interceptors;

    public HasordbAutoConfiguration(HasordbProperties properties) {
        this.properties = properties;
        // this.interceptors = (Interceptor[]) interceptorsProvider.getIfAvailable();
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void afterPropertiesSet() {
        //
        //hasordb.mapper-locations=classpath:hasordb/mapper/*.xml
        //hasordb.mapper-packages=com.example.demo.dao.*
        //
        //hasordb.type-handlers-packages=com.example.demo.dao.*
        //hasordb.type-handlers
    }

    @Bean
    @ConditionalOnMissingBean
    public DalRegistry dalRegistry(ObjectProvider<TypeHandlerRegistry> typeHandlersProvider, ObjectProvider<RuleRegistry> ruleRegistryProvider) {
        TypeHandlerRegistry typeHandlerRegistry = typeHandlersProvider.getIfAvailable();
        RuleRegistry ruleRegistry = ruleRegistryProvider.getIfAvailable();

        MappingOptions options = MappingOptions.buildNew();
        return new DalRegistry(this.classLoader, typeHandlerRegistry, ruleRegistry, options);
    }

    @Bean
    @ConditionalOnMissingBean
    public DalSession dalSession(DataSource dataSource, DalRegistry dalRegistry) throws Exception {
        return new DalSession(dataSource, dalRegistry);
    }

    //  jdbcTemplate name of the conflict

    @Bean
    @ConditionalOnMissingBean
    public LambdaTemplate lambdaTemplate(DataSource dataSource, ObjectProvider<TypeHandlerRegistry> typeHandlersProvider) {
        TypeHandlerRegistry typeHandlerRegistry = typeHandlersProvider.getIfAvailable();
        if (typeHandlerRegistry == null) {
            return new LambdaTemplate(dataSource);
        } else {
            return new LambdaTemplate(dataSource, typeHandlerRegistry);
        }
    }

    /**
     * If mapper registering configuration or mapper scanning configuration not present,
     * this configuration allow to scan mappers based on the same component-scanning path as Spring Boot itself.
     */
    @Configuration
    @Import(AutoConfiguredMapperScannerRegistrar.class)
    @ConditionalOnMissingBean({ DalSession.class, MapperScannerConfigurer.class })
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {
        public void afterPropertiesSet() {
            logger.debug("Not found configuration for registering mapper bean using @MapperScan, DalSessionBean and MapperScannerConfigurer.");
        }
    }

    public static class AutoConfiguredMapperScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {
        private BeanFactory beanFactory;

        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.");
                return;
            }

            logger.debug("Searching for mappers annotated with @DalMapper");
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            if (logger.isDebugEnabled()) {
                packages.forEach(pkg -> logger.debug("Using auto-configuration base package '" + pkg + "'"));
            }

            String mapperBeanName = MapperScannerConfigurer.class.getName() + "#_auto";
            String fileBeanName = MapperFileConfigurer.class.getName() + "#_auto";

            BeanDefinitionBuilder mapperBuilder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
            mapperBuilder.addPropertyValue("processPropertyPlaceHolders", true);
            mapperBuilder.addPropertyValue("basePackage", "${hasordb.mapper-packages:" + StringUtils.collectionToCommaDelimitedString(packages) + "}");
            mapperBuilder.addPropertyValue("mapperFactoryBeanClassName", "${hasordb.mapper-factory-bean:}");
            mapperBuilder.addPropertyValue("defaultScope", "${hasordb.mapper-scope:}");
            mapperBuilder.addPropertyValue("lazyInitialization", "${hasordb.mapper-lazy-initialization:false}");
            mapperBuilder.addPropertyValue("nameGeneratorName", "${hasordb.mapper-name-generator:}");
            mapperBuilder.addPropertyValue("annotationClassName", "${hasordb.marker-annotation:" + DalMapper.class.getName() + "}");
            mapperBuilder.addPropertyValue("markerInterfaceName", "${hasordb.marker-interface:}");
            mapperBuilder.addPropertyValue("dalSessionRef", "${hasordb.ref-session-bean:}");
            mapperBuilder.addPropertyValue("dependsOn", fileBeanName);
            registry.registerBeanDefinition(mapperBeanName, mapperBuilder.getBeanDefinition());

            BeanDefinitionBuilder fileBuilder = BeanDefinitionBuilder.genericBeanDefinition(MapperFileConfigurer.class);
            mapperBuilder.addPropertyValue("processPropertyPlaceHolders", true);
            fileBuilder.addPropertyValue("mapperLocations", "${hasordb.mapper-locations:classpath:**/*.xml}");
            fileBuilder.addPropertyValue("dalSessionRef", "${hasordb.ref-session-bean:}");
            registry.registerBeanDefinition(fileBeanName, fileBuilder.getBeanDefinition());
        }
    }

}