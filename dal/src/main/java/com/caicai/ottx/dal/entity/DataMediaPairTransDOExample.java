/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataMediaPairTransDOExample.java
 * 
 */
package com.caicai.ottx.dal.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataMediaPairTransDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DataMediaPairTransDOExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdIsNull() {
            addCriterion("SOURCE_DATA_MEDIA_ID is null");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdIsNotNull() {
            addCriterion("SOURCE_DATA_MEDIA_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdEqualTo(Long value) {
            addCriterion("SOURCE_DATA_MEDIA_ID =", value, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdNotEqualTo(Long value) {
            addCriterion("SOURCE_DATA_MEDIA_ID <>", value, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdGreaterThan(Long value) {
            addCriterion("SOURCE_DATA_MEDIA_ID >", value, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdGreaterThanOrEqualTo(Long value) {
            addCriterion("SOURCE_DATA_MEDIA_ID >=", value, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdLessThan(Long value) {
            addCriterion("SOURCE_DATA_MEDIA_ID <", value, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdLessThanOrEqualTo(Long value) {
            addCriterion("SOURCE_DATA_MEDIA_ID <=", value, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdIn(List<Long> values) {
            addCriterion("SOURCE_DATA_MEDIA_ID in", values, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdNotIn(List<Long> values) {
            addCriterion("SOURCE_DATA_MEDIA_ID not in", values, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdBetween(Long value1, Long value2) {
            addCriterion("SOURCE_DATA_MEDIA_ID between", value1, value2, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andSourceDataMediaIdNotBetween(Long value1, Long value2) {
            addCriterion("SOURCE_DATA_MEDIA_ID not between", value1, value2, "sourceDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdIsNull() {
            addCriterion("TARGET_DATA_MEDIA_ID is null");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdIsNotNull() {
            addCriterion("TARGET_DATA_MEDIA_ID is not null");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdEqualTo(Long value) {
            addCriterion("TARGET_DATA_MEDIA_ID =", value, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdNotEqualTo(Long value) {
            addCriterion("TARGET_DATA_MEDIA_ID <>", value, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdGreaterThan(Long value) {
            addCriterion("TARGET_DATA_MEDIA_ID >", value, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdGreaterThanOrEqualTo(Long value) {
            addCriterion("TARGET_DATA_MEDIA_ID >=", value, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdLessThan(Long value) {
            addCriterion("TARGET_DATA_MEDIA_ID <", value, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdLessThanOrEqualTo(Long value) {
            addCriterion("TARGET_DATA_MEDIA_ID <=", value, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdIn(List<Long> values) {
            addCriterion("TARGET_DATA_MEDIA_ID in", values, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdNotIn(List<Long> values) {
            addCriterion("TARGET_DATA_MEDIA_ID not in", values, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdBetween(Long value1, Long value2) {
            addCriterion("TARGET_DATA_MEDIA_ID between", value1, value2, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andTargetDataMediaIdNotBetween(Long value1, Long value2) {
            addCriterion("TARGET_DATA_MEDIA_ID not between", value1, value2, "targetDataMediaId");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeIsNull() {
            addCriterion("COLUMN_PAIR_MODE is null");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeIsNotNull() {
            addCriterion("COLUMN_PAIR_MODE is not null");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeEqualTo(String value) {
            addCriterion("COLUMN_PAIR_MODE =", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeNotEqualTo(String value) {
            addCriterion("COLUMN_PAIR_MODE <>", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeGreaterThan(String value) {
            addCriterion("COLUMN_PAIR_MODE >", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeGreaterThanOrEqualTo(String value) {
            addCriterion("COLUMN_PAIR_MODE >=", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeLessThan(String value) {
            addCriterion("COLUMN_PAIR_MODE <", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeLessThanOrEqualTo(String value) {
            addCriterion("COLUMN_PAIR_MODE <=", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeLike(String value) {
            addCriterion("COLUMN_PAIR_MODE like", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeNotLike(String value) {
            addCriterion("COLUMN_PAIR_MODE not like", value, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeIn(List<String> values) {
            addCriterion("COLUMN_PAIR_MODE in", values, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeNotIn(List<String> values) {
            addCriterion("COLUMN_PAIR_MODE not in", values, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeBetween(String value1, String value2) {
            addCriterion("COLUMN_PAIR_MODE between", value1, value2, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andColumnPairModeNotBetween(String value1, String value2) {
            addCriterion("COLUMN_PAIR_MODE not between", value1, value2, "columnPairMode");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("GMT_CREATE is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("GMT_CREATE is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("GMT_CREATE =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("GMT_CREATE <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("GMT_CREATE >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("GMT_CREATE >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("GMT_CREATE <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("GMT_CREATE <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("GMT_CREATE in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("GMT_CREATE not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("GMT_CREATE between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("GMT_CREATE not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNull() {
            addCriterion("GMT_MODIFIED is null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNotNull() {
            addCriterion("GMT_MODIFIED is not null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedEqualTo(Date value) {
            addCriterion("GMT_MODIFIED =", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotEqualTo(Date value) {
            addCriterion("GMT_MODIFIED <>", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThan(Date value) {
            addCriterion("GMT_MODIFIED >", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("GMT_MODIFIED >=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThan(Date value) {
            addCriterion("GMT_MODIFIED <", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThanOrEqualTo(Date value) {
            addCriterion("GMT_MODIFIED <=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIn(List<Date> values) {
            addCriterion("GMT_MODIFIED in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotIn(List<Date> values) {
            addCriterion("GMT_MODIFIED not in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedBetween(Date value1, Date value2) {
            addCriterion("GMT_MODIFIED between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotBetween(Date value1, Date value2) {
            addCriterion("GMT_MODIFIED not between", value1, value2, "gmtModified");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}