/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * TableHistoryStatDOExample.java
 * 
 */
package com.caicai.ottx.dal.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableHistoryStatDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TableHistoryStatDOExample() {
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

        public Criteria andFileSizeIsNull() {
            addCriterion("FILE_SIZE is null");
            return (Criteria) this;
        }

        public Criteria andFileSizeIsNotNull() {
            addCriterion("FILE_SIZE is not null");
            return (Criteria) this;
        }

        public Criteria andFileSizeEqualTo(Long value) {
            addCriterion("FILE_SIZE =", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotEqualTo(Long value) {
            addCriterion("FILE_SIZE <>", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThan(Long value) {
            addCriterion("FILE_SIZE >", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("FILE_SIZE >=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThan(Long value) {
            addCriterion("FILE_SIZE <", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThanOrEqualTo(Long value) {
            addCriterion("FILE_SIZE <=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeIn(List<Long> values) {
            addCriterion("FILE_SIZE in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotIn(List<Long> values) {
            addCriterion("FILE_SIZE not in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeBetween(Long value1, Long value2) {
            addCriterion("FILE_SIZE between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotBetween(Long value1, Long value2) {
            addCriterion("FILE_SIZE not between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileCountIsNull() {
            addCriterion("FILE_COUNT is null");
            return (Criteria) this;
        }

        public Criteria andFileCountIsNotNull() {
            addCriterion("FILE_COUNT is not null");
            return (Criteria) this;
        }

        public Criteria andFileCountEqualTo(Long value) {
            addCriterion("FILE_COUNT =", value, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountNotEqualTo(Long value) {
            addCriterion("FILE_COUNT <>", value, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountGreaterThan(Long value) {
            addCriterion("FILE_COUNT >", value, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountGreaterThanOrEqualTo(Long value) {
            addCriterion("FILE_COUNT >=", value, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountLessThan(Long value) {
            addCriterion("FILE_COUNT <", value, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountLessThanOrEqualTo(Long value) {
            addCriterion("FILE_COUNT <=", value, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountIn(List<Long> values) {
            addCriterion("FILE_COUNT in", values, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountNotIn(List<Long> values) {
            addCriterion("FILE_COUNT not in", values, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountBetween(Long value1, Long value2) {
            addCriterion("FILE_COUNT between", value1, value2, "fileCount");
            return (Criteria) this;
        }

        public Criteria andFileCountNotBetween(Long value1, Long value2) {
            addCriterion("FILE_COUNT not between", value1, value2, "fileCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountIsNull() {
            addCriterion("INSERT_COUNT is null");
            return (Criteria) this;
        }

        public Criteria andInsertCountIsNotNull() {
            addCriterion("INSERT_COUNT is not null");
            return (Criteria) this;
        }

        public Criteria andInsertCountEqualTo(Long value) {
            addCriterion("INSERT_COUNT =", value, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountNotEqualTo(Long value) {
            addCriterion("INSERT_COUNT <>", value, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountGreaterThan(Long value) {
            addCriterion("INSERT_COUNT >", value, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountGreaterThanOrEqualTo(Long value) {
            addCriterion("INSERT_COUNT >=", value, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountLessThan(Long value) {
            addCriterion("INSERT_COUNT <", value, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountLessThanOrEqualTo(Long value) {
            addCriterion("INSERT_COUNT <=", value, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountIn(List<Long> values) {
            addCriterion("INSERT_COUNT in", values, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountNotIn(List<Long> values) {
            addCriterion("INSERT_COUNT not in", values, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountBetween(Long value1, Long value2) {
            addCriterion("INSERT_COUNT between", value1, value2, "insertCount");
            return (Criteria) this;
        }

        public Criteria andInsertCountNotBetween(Long value1, Long value2) {
            addCriterion("INSERT_COUNT not between", value1, value2, "insertCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountIsNull() {
            addCriterion("UPDATE_COUNT is null");
            return (Criteria) this;
        }

        public Criteria andUpdateCountIsNotNull() {
            addCriterion("UPDATE_COUNT is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateCountEqualTo(Long value) {
            addCriterion("UPDATE_COUNT =", value, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountNotEqualTo(Long value) {
            addCriterion("UPDATE_COUNT <>", value, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountGreaterThan(Long value) {
            addCriterion("UPDATE_COUNT >", value, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountGreaterThanOrEqualTo(Long value) {
            addCriterion("UPDATE_COUNT >=", value, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountLessThan(Long value) {
            addCriterion("UPDATE_COUNT <", value, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountLessThanOrEqualTo(Long value) {
            addCriterion("UPDATE_COUNT <=", value, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountIn(List<Long> values) {
            addCriterion("UPDATE_COUNT in", values, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountNotIn(List<Long> values) {
            addCriterion("UPDATE_COUNT not in", values, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountBetween(Long value1, Long value2) {
            addCriterion("UPDATE_COUNT between", value1, value2, "updateCount");
            return (Criteria) this;
        }

        public Criteria andUpdateCountNotBetween(Long value1, Long value2) {
            addCriterion("UPDATE_COUNT not between", value1, value2, "updateCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountIsNull() {
            addCriterion("DELETE_COUNT is null");
            return (Criteria) this;
        }

        public Criteria andDeleteCountIsNotNull() {
            addCriterion("DELETE_COUNT is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteCountEqualTo(Long value) {
            addCriterion("DELETE_COUNT =", value, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountNotEqualTo(Long value) {
            addCriterion("DELETE_COUNT <>", value, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountGreaterThan(Long value) {
            addCriterion("DELETE_COUNT >", value, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountGreaterThanOrEqualTo(Long value) {
            addCriterion("DELETE_COUNT >=", value, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountLessThan(Long value) {
            addCriterion("DELETE_COUNT <", value, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountLessThanOrEqualTo(Long value) {
            addCriterion("DELETE_COUNT <=", value, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountIn(List<Long> values) {
            addCriterion("DELETE_COUNT in", values, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountNotIn(List<Long> values) {
            addCriterion("DELETE_COUNT not in", values, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountBetween(Long value1, Long value2) {
            addCriterion("DELETE_COUNT between", value1, value2, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDeleteCountNotBetween(Long value1, Long value2) {
            addCriterion("DELETE_COUNT not between", value1, value2, "deleteCount");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdIsNull() {
            addCriterion("DATA_MEDIA_PAIR_ID is null");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdIsNotNull() {
            addCriterion("DATA_MEDIA_PAIR_ID is not null");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdEqualTo(Long value) {
            addCriterion("DATA_MEDIA_PAIR_ID =", value, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdNotEqualTo(Long value) {
            addCriterion("DATA_MEDIA_PAIR_ID <>", value, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdGreaterThan(Long value) {
            addCriterion("DATA_MEDIA_PAIR_ID >", value, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdGreaterThanOrEqualTo(Long value) {
            addCriterion("DATA_MEDIA_PAIR_ID >=", value, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdLessThan(Long value) {
            addCriterion("DATA_MEDIA_PAIR_ID <", value, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdLessThanOrEqualTo(Long value) {
            addCriterion("DATA_MEDIA_PAIR_ID <=", value, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdIn(List<Long> values) {
            addCriterion("DATA_MEDIA_PAIR_ID in", values, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdNotIn(List<Long> values) {
            addCriterion("DATA_MEDIA_PAIR_ID not in", values, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdBetween(Long value1, Long value2) {
            addCriterion("DATA_MEDIA_PAIR_ID between", value1, value2, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andDataMediaPairIdNotBetween(Long value1, Long value2) {
            addCriterion("DATA_MEDIA_PAIR_ID not between", value1, value2, "dataMediaPairId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdIsNull() {
            addCriterion("PIPELINE_ID is null");
            return (Criteria) this;
        }

        public Criteria andPipelineIdIsNotNull() {
            addCriterion("PIPELINE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andPipelineIdEqualTo(Long value) {
            addCriterion("PIPELINE_ID =", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotEqualTo(Long value) {
            addCriterion("PIPELINE_ID <>", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdGreaterThan(Long value) {
            addCriterion("PIPELINE_ID >", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdGreaterThanOrEqualTo(Long value) {
            addCriterion("PIPELINE_ID >=", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdLessThan(Long value) {
            addCriterion("PIPELINE_ID <", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdLessThanOrEqualTo(Long value) {
            addCriterion("PIPELINE_ID <=", value, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdIn(List<Long> values) {
            addCriterion("PIPELINE_ID in", values, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotIn(List<Long> values) {
            addCriterion("PIPELINE_ID not in", values, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdBetween(Long value1, Long value2) {
            addCriterion("PIPELINE_ID between", value1, value2, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andPipelineIdNotBetween(Long value1, Long value2) {
            addCriterion("PIPELINE_ID not between", value1, value2, "pipelineId");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("START_TIME is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("START_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("START_TIME =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("START_TIME <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("START_TIME >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("START_TIME >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("START_TIME <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("START_TIME <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("START_TIME in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("START_TIME not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("START_TIME between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("START_TIME not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("END_TIME is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("END_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("END_TIME =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("END_TIME <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("END_TIME >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("END_TIME >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("END_TIME <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("END_TIME <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("END_TIME in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("END_TIME not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("END_TIME between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("END_TIME not between", value1, value2, "endTime");
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