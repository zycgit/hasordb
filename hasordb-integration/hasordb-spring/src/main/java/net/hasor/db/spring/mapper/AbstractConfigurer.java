package net.hasor.db.spring.mapper;
import net.hasor.db.spring.annotation.MapperScan;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/** A resource load for {@link MapperScan}. */
public class AbstractConfigurer implements ApplicationContextAware, BeanClassLoaderAware, BeanNameAware {
    protected ApplicationContext applicationContext;
    protected ClassLoader        classLoader;
    protected String             beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected Environment getEnvironment() {
        return this.applicationContext.getEnvironment();
    }

    protected String getPropertyValue(String propertyName, PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value.toString();
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else {
            return null;
        }
    }

    protected Class<?> tryToClass(String className) {
        if (!StringUtils.hasText(className)) {
            return null;
        }
        try {
            return this.classLoader.loadClass(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
