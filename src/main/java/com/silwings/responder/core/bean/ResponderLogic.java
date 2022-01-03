package com.silwings.responder.core.bean;

import com.silwings.responder.commons.bean.BaseBean;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ResponderLogic
 * @Description 返回值判断逻辑
 * @Author Silwings
 * @Date 2021/8/7 11:25
 * @Version V1.0
 **/
public class ResponderLogic extends BaseBean {

    /**
     * 逻辑描述
     */
    private String logic;

    /**
     * 解析后的逻辑列表
     */
    private List<Logic> logicList;

    private static final String RETURN_SYMBOL = " return ";
    private static final String THROW_SYMBOL = " throw ";
    private static final String EQUALS_SYMBOL = " == ";
    private static final String NO_EQUALS_SYMBOL = " != ";


    public List<Logic> findLogic() {
        if (!this.hasLogic()) {
            return Collections.emptyList();
        }
        if (null != this.getLogicList()) {
            return this.getLogicList();
        }

        final List<Logic> result = new ArrayList<>();


        final List<String> logicArrayList = Arrays.asList(this.getLogic().split("\n"));
        for (String logicStr : logicArrayList) {
            if (!logicStr.contains(RETURN_SYMBOL) && !logicStr.contains(THROW_SYMBOL)) {
                // 既没有返回值也没有异常的忽略
                continue;
            }
            if (logicStr.contains(RETURN_SYMBOL) && logicStr.contains(THROW_SYMBOL)) {
                // 同时有返回值和异常的忽略
                continue;
            }
            if (!logicStr.contains(EQUALS_SYMBOL) && !logicStr.contains(NO_EQUALS_SYMBOL)) {
                // 没有可用条件忽略
                continue;
            }

            final String splitStr = logicStr.contains(RETURN_SYMBOL) ? RETURN_SYMBOL : THROW_SYMBOL;

            final String[] split = logicStr.split(splitStr);
            if (split.length < 2) {
                // 长度不符
                continue;
            }
            String resultKey = split[1];

            final Logic.Builder builder = Logic.builder(resultKey);
            builder.setThrowFlag(splitStr);

            final String condition = split[0];

            if (condition.contains("&")) {
                final String[] fragmentArr = condition.split("&");

                for (String fragment : fragmentArr) {
                    this.splitCondition(builder, fragment);
                }
            } else {
                this.splitCondition(builder, condition);
            }
            if (builder.normal()) {
                result.add(builder.build());
            }
        }
        this.setLogicList(result);
        return this.getLogicList();
    }


    private void splitCondition(Logic.Builder builder, String condition) {
        if (condition.contains(EQUALS_SYMBOL)) {
            final String[] eqArr = condition.split(EQUALS_SYMBOL);
            if (eqArr.length < 2) {
                // 长度不符
                return;
            }
            builder.putEqKv(eqArr[0], eqArr[1]);
        } else if (condition.contains(NO_EQUALS_SYMBOL)) {
            final String[] difArr = condition.split(NO_EQUALS_SYMBOL);
            if (difArr.length < 2) {
                // 长度不符
                return;
            }
            builder.putDifKv(difArr[0], difArr[1]);
        }
    }

    public void setLogic(String logic) {
        if (null != this.logic) {
            throw new IllegalStateException("判断逻辑不可修改");
        }

        // 转换换行符
        this.logic = logic.replace("\r\n", "\n")
                .replace("\r", "\n");
    }

    public boolean hasLogic() {
        return StringUtils.isNotBlank(this.getLogic());
    }

    public String getLogic() {
        return this.logic;
    }


    public static class Logic {

        private Map<String, String> eqKv;

        private Map<String, String> differentKv;

        private String resultKey;

        private boolean throwFlag;

        private Logic(Builder builder) {
            this.eqKv = builder.eqKv;
            this.differentKv = builder.differentKv;
            this.resultKey = builder.resultKey;
            this.throwFlag = builder.throwFlag;
        }

        public String getResultKey() {
            return resultKey;
        }

        public static Builder builder(String resultKey) {
            return new Builder(resultKey);
        }

        public Map<String, String> getEqualsCondition() {
            return new HashMap<>(this.getEqKv());
        }

        public Map<String, String> getDifferentCondition() {
            return new HashMap<>(this.getDifferentKv());
        }

        protected Map<String, String> getEqKv() {
            return eqKv;
        }

        protected Map<String, String> getDifferentKv() {
            return differentKv;
        }

        public boolean isThrow() {
            return this.throwFlag;
        }

        private static class Builder {
            private Map<String, String> eqKv;

            private Map<String, String> differentKv;

            private String resultKey;

            private boolean throwFlag;

            public Builder(String resultKey) {
                this.eqKv = new HashMap<>();
                this.differentKv = new HashMap<>();
                this.resultKey = resultKey;
            }

            public Builder putEqKv(String paeramKey, String value) {
                this.eqKv.put(null == paeramKey ? null : paeramKey.trim(), null == value ? null : value.trim());
                return this;
            }

            public Builder putDifKv(String paeramKey, String value) {
                this.differentKv.put(null == paeramKey ? null : paeramKey.trim(), null == value ? null : value.trim());
                return this;
            }

            public Logic build() {
                return new Logic(this);
            }


            public boolean normal() {
                return this.eqKv.size() > 0 || this.differentKv.size() > 0;
            }

            private void setThrowFlag(String splitStr) {
                this.setThrowFlag(THROW_SYMBOL.trim().equalsIgnoreCase(splitStr.trim()));
            }

            private void setThrowFlag(boolean flag) {
                this.throwFlag = flag;
            }
        }
    }

    private List<Logic> getLogicList() {
        return this.logicList;
    }

    private void setLogicList(List<Logic> result) {
        this.logicList = result;
    }

}
