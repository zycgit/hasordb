package net.hasor.db.page;
import net.hasor.utils.function.ESupplier;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PageObject implements Page {
    /** 满足条件的总记录数 */
    private       ESupplier<Integer, SQLException> totalCountSupplier = () -> 0;
    private       int                              totalCount         = -1;
    private final AtomicBoolean                    totalCountInited   = new AtomicBoolean(false);
    /** 每页记录数（-1表示无限大）*/
    private       int                              pageSize           = -1;
    /** 当前页号 */
    private       int                              currentPage        = 0;
    /** 起始页码的偏移量 */
    private       int                              pageNumberOffset   = 0;

    public PageObject() {
        this.totalCountSupplier = () -> -1;
    }

    public PageObject(int pageSize, ESupplier<Integer, SQLException> totalCountSupplier) {
        Objects.requireNonNull(totalCountSupplier, "totalCountSupplier is null.");
        this.pageSize = pageSize;
        this.totalCountSupplier = totalCountSupplier;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    /** 设置分页的页大小 */
    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = 1;
        }
        this.pageSize = pageSize;
    }

    /**取当前页号 */
    public int getCurrentPage() {
        return this.currentPage;
    }

    /** 设置前页号 */
    public void setCurrentPage(int currentPage) {
        if (currentPage <= this.pageNumberOffset) {
            this.currentPage = 0;
        } else {
            this.currentPage = currentPage - this.pageNumberOffset;
        }
    }

    public int getPageNumberOffset() {
        return this.pageNumberOffset;
    }

    public void setPageNumberOffset(int pageNumberOffset) {
        this.pageNumberOffset = Math.max(pageNumberOffset, 0);
    }

    /** 获取本页第一个记录的索引位置 */
    public int getFirstRecordPosition() {
        int pgSize = getPageSize();
        if (pgSize < 0) {
            return 0;
        }
        return pgSize * getCurrentPage();
    }

    /** 获取总页数 */
    public int getTotalPage() throws SQLException {
        int pgSize = getPageSize();
        int result = 1;
        if (pgSize > 0) {
            int totalCount = totalCount();
            result = totalCount() / pgSize;
            if ((totalCount == 0) || ((totalCount % pgSize) != 0)) {
                result++;
            }
        }
        return result + this.pageNumberOffset;
    }

    /** 获取记录总数 */
    public int totalCount() throws SQLException {
        if (this.totalCountInited.compareAndSet(false, true)) {
            this.totalCount = this.totalCountSupplier.eGet();
        }
        return this.totalCount;
    }
}
