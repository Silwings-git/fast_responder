package com.silwings.web.bean.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName PageData
 * @Description 分页数据
 * @Author Silwings
 * @Date 2022/1/8 16:44
 * @Version V1.0
 **/
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageData<T> {

    private List<T> list;

    private Long total;

    public List<T> getList() {
        return CollectionUtils.isEmpty(this.list) ? Collections.emptyList() : this.list;
    }

    public Long getTotal() {
        return null == this.total ? 0L : this.total;
    }

    public static <T> PageData<T> empty() {
        return new PageData<>();
    }

}
