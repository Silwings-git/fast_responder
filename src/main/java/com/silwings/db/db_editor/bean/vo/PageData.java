package com.silwings.db.db_editor.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * PageData<T>
 *
 * @author Andrew.Dong
 * @Date 2019-10-07 16:54
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageData<T> {

    private List<T> list;

    private Long total;
}
