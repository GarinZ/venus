package art.meiye.venus.dal.entity;

import java.util.ArrayList;
import java.util.List;

public class UserIdentityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserIdentityExample() {
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

        public Criteria andUserIdentityIdIsNull() {
            addCriterion("user_identity_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdIsNotNull() {
            addCriterion("user_identity_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdEqualTo(Integer value) {
            addCriterion("user_identity_id =", value, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdNotEqualTo(Integer value) {
            addCriterion("user_identity_id <>", value, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdGreaterThan(Integer value) {
            addCriterion("user_identity_id >", value, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_identity_id >=", value, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdLessThan(Integer value) {
            addCriterion("user_identity_id <", value, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_identity_id <=", value, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdIn(List<Integer> values) {
            addCriterion("user_identity_id in", values, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdNotIn(List<Integer> values) {
            addCriterion("user_identity_id not in", values, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdBetween(Integer value1, Integer value2) {
            addCriterion("user_identity_id between", value1, value2, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_identity_id not between", value1, value2, "userIdentityId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeIsNull() {
            addCriterion("user_identity_type is null");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeIsNotNull() {
            addCriterion("user_identity_type is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeEqualTo(Integer value) {
            addCriterion("user_identity_type =", value, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeNotEqualTo(Integer value) {
            addCriterion("user_identity_type <>", value, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeGreaterThan(Integer value) {
            addCriterion("user_identity_type >", value, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_identity_type >=", value, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeLessThan(Integer value) {
            addCriterion("user_identity_type <", value, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeLessThanOrEqualTo(Integer value) {
            addCriterion("user_identity_type <=", value, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeIn(List<Integer> values) {
            addCriterion("user_identity_type in", values, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeNotIn(List<Integer> values) {
            addCriterion("user_identity_type not in", values, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeBetween(Integer value1, Integer value2) {
            addCriterion("user_identity_type between", value1, value2, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andUserIdentityTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("user_identity_type not between", value1, value2, "userIdentityType");
            return (Criteria) this;
        }

        public Criteria andIdentifierIsNull() {
            addCriterion("identifier is null");
            return (Criteria) this;
        }

        public Criteria andIdentifierIsNotNull() {
            addCriterion("identifier is not null");
            return (Criteria) this;
        }

        public Criteria andIdentifierEqualTo(String value) {
            addCriterion("identifier =", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierNotEqualTo(String value) {
            addCriterion("identifier <>", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierGreaterThan(String value) {
            addCriterion("identifier >", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierGreaterThanOrEqualTo(String value) {
            addCriterion("identifier >=", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierLessThan(String value) {
            addCriterion("identifier <", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierLessThanOrEqualTo(String value) {
            addCriterion("identifier <=", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierLike(String value) {
            addCriterion("identifier like", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierNotLike(String value) {
            addCriterion("identifier not like", value, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierIn(List<String> values) {
            addCriterion("identifier in", values, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierNotIn(List<String> values) {
            addCriterion("identifier not in", values, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierBetween(String value1, String value2) {
            addCriterion("identifier between", value1, value2, "identifier");
            return (Criteria) this;
        }

        public Criteria andIdentifierNotBetween(String value1, String value2) {
            addCriterion("identifier not between", value1, value2, "identifier");
            return (Criteria) this;
        }

        public Criteria andCredentialIsNull() {
            addCriterion("credential is null");
            return (Criteria) this;
        }

        public Criteria andCredentialIsNotNull() {
            addCriterion("credential is not null");
            return (Criteria) this;
        }

        public Criteria andCredentialEqualTo(String value) {
            addCriterion("credential =", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialNotEqualTo(String value) {
            addCriterion("credential <>", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialGreaterThan(String value) {
            addCriterion("credential >", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialGreaterThanOrEqualTo(String value) {
            addCriterion("credential >=", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialLessThan(String value) {
            addCriterion("credential <", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialLessThanOrEqualTo(String value) {
            addCriterion("credential <=", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialLike(String value) {
            addCriterion("credential like", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialNotLike(String value) {
            addCriterion("credential not like", value, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialIn(List<String> values) {
            addCriterion("credential in", values, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialNotIn(List<String> values) {
            addCriterion("credential not in", values, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialBetween(String value1, String value2) {
            addCriterion("credential between", value1, value2, "credential");
            return (Criteria) this;
        }

        public Criteria andCredentialNotBetween(String value1, String value2) {
            addCriterion("credential not between", value1, value2, "credential");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Integer value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Integer value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Integer value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Integer value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Integer value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Integer> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Integer> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Integer value1, Integer value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Integer value1, Integer value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltIsNull() {
            addCriterion("credential_salt is null");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltIsNotNull() {
            addCriterion("credential_salt is not null");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltEqualTo(String value) {
            addCriterion("credential_salt =", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltNotEqualTo(String value) {
            addCriterion("credential_salt <>", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltGreaterThan(String value) {
            addCriterion("credential_salt >", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltGreaterThanOrEqualTo(String value) {
            addCriterion("credential_salt >=", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltLessThan(String value) {
            addCriterion("credential_salt <", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltLessThanOrEqualTo(String value) {
            addCriterion("credential_salt <=", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltLike(String value) {
            addCriterion("credential_salt like", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltNotLike(String value) {
            addCriterion("credential_salt not like", value, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltIn(List<String> values) {
            addCriterion("credential_salt in", values, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltNotIn(List<String> values) {
            addCriterion("credential_salt not in", values, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltBetween(String value1, String value2) {
            addCriterion("credential_salt between", value1, value2, "credentialSalt");
            return (Criteria) this;
        }

        public Criteria andCredentialSaltNotBetween(String value1, String value2) {
            addCriterion("credential_salt not between", value1, value2, "credentialSalt");
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