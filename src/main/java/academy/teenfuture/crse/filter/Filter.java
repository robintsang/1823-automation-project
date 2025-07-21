package academy.teenfuture.crse.filter;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Filter {
    public static <T> Slice<T> getSlicePage(JpaSpecificationExecutor<T> repository, Specification<T> spec, String sort, String sortMode, int page, int size) {
        Sort.Direction sortDir = Sort.Direction.fromOptionalString(sortMode).orElse(Sort.Direction.ASC);
        if (size == -1) {
            List<T> allTransactions = repository.findAll(spec, Sort.by(sortDir, sort));
            return new SliceImpl<>(allTransactions);
        } else {
            Pageable pageable = PageRequest.of(page, size, sortDir, sort);
            return repository.findAll(spec, pageable);
        }
    }

    public static <T> Specification<T> getFilterSearch(Class<T> entityClass, Map<String, String> filterParams, String search) {
        Specification<T> spec = Filter.getFilterByParams(filterParams);
        return spec.and(Filter.search(search, entityClass));
    }

    public static <T> Specification<T> getFilterByParams(Map<String, String> params) {
        Specification<T> spec = Specification.where(null);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            if (!field.contains(":")) {
                continue;
            }

            String[] fieldParts = field.split(":");
            String fieldName = fieldParts[0];
            String operator = fieldParts[1];
            Specification<T> filter = getFilterByField(fieldName, operator, value);
            spec = spec.and(filter);
        }

        return spec;
    }

    static <T> Specification<T> getFilterByField(String fieldName, String operator, String value) {
        return (root, query, builder) -> {
            Path<?> path = root.get(fieldName);

            if (path == null) {
                System.out.println("field name not found " + fieldName);
                return null;
            }

            if (path.getJavaType().equals(String.class)) {
                return getStringFilterByField(fieldName, operator, value, root, builder);
            } else if (Number.class.isAssignableFrom(path.getJavaType()) && value.matches("-?\\d+(\\.\\d+)?")) {
                return getNumberFilterByField(fieldName, operator, value, root, builder);
            } else if (path.getJavaType().isEnum()) {
                return getEnumFilterByField(fieldName, operator, value, root, builder);
            } else if (path.getJavaType().equals(Date.class)) {
                return getDateFilterByField(fieldName, operator, value, root, builder);
            } else {
                throw new IllegalArgumentException("Unsupported field type: " + path.getJavaType());
            }
        };
    }

    static <T> Predicate getStringFilterByField(String fieldName, String operator, String value, Root<T> root, CriteriaBuilder builder) {
        Path<String> path = root.get(fieldName);
        switch (operator) {
            case "contains":
                return builder.like(builder.lower(path), "%" + value.toLowerCase() + "%");
            case "equals":
                return builder.equal(path, value);
            case "startsWith":
                return builder.like(builder.lower(path), value.toLowerCase() + "%");
            case "endsWith":
                return builder.like(builder.lower(path), "%" + value.toLowerCase());
            case "isEmpty":
                return builder.or(builder.isNull(path), builder.equal(path, ""));
            case "isNotEmpty":
                return builder.and(builder.isNotNull(path), builder.notEqual(path, ""));
            case "isAnyOf":
                String[] values = value.split(",");
                Predicate[] predicates = new Predicate[values.length];
                for (int i = 0; i < values.length; i++) {
                    predicates[i] = builder.equal(path, values[i]);
                }
                return builder.or(predicates);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    static <T> Predicate getEnumFilterByField(String fieldName, String operator, String value, Root<T> root, CriteriaBuilder builder) {
        Path<String> path = root.get(fieldName);
        switch (operator) {
            case "is":
                return builder.like(builder.lower(path), value.toLowerCase());
            case "isNot":
                return builder.not(builder.like(builder.lower(path), value.toLowerCase()));
            case "isAnyOf":
                String[] values = value.split(",");
                Predicate[] predicates = new Predicate[values.length];
                for (int i = 0; i < values.length; i++) {
                    predicates[i] = builder.like(builder.lower(path), values[i].toLowerCase());
                }
                return builder.or(predicates);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    static <T> Predicate getNumberFilterByField(String fieldName, String operator, String value, Root<T> root, CriteriaBuilder builder) {
        Path<Number> path = root.get(fieldName);
        Number numberValue = null;
        if (value.contains(".")) {
            numberValue = Double.parseDouble(value);
        } else {
            numberValue = Long.parseLong(value);
        }
        switch (operator) {
            case "=":
                return builder.equal(path, numberValue);
            case "!=":
                return builder.notEqual(path, numberValue);
            case ">":
                return builder.gt(path, numberValue);
            case ">=":
                return builder.ge(path, numberValue);
            case "<":
                return builder.lt(path, numberValue);
            case "<=":
                return builder.le(path, numberValue);
            case "isEmpty":
                return builder.or(builder.isNull(path), builder.equal(path, 0));
            case "isNotEmpty":
                return builder.and(builder.isNotNull(path), builder.notEqual(path, 0));
            case "isAnyOf":
                String[] values = value.split(",");
                Predicate[] predicates = new Predicate[values.length];
                for (int i = 0; i < values.length; i++) {
                    Number num = null;
                    if (values[i].contains(".")) {
                        num = Double.parseDouble(values[i]);
                    } else {
                        num = Long.parseLong(values[i]);
                    }
                    predicates[i] = builder.equal(path, num);
                }
                return builder.or(predicates);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    static <T> Predicate getDateFilterByField(String fieldName, String operator, String value, Root<T> root, CriteriaBuilder builder) {
        Path<Date> path = root.get(fieldName);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = null;
        try {
            dateValue = dateFormat.parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + value);
        }

        switch (operator) {
            case "is":
                return builder.equal(path, dateValue);
            case "isNot":
                return builder.notEqual(path, dateValue);
            case "isAfter":
                return builder.greaterThan(path, dateValue);
            case "isOnOrAfter":
                return builder.greaterThanOrEqualTo(path, dateValue);
            case "isBefore":
                return builder.lessThan(path, dateValue);
            case "isOnOrBefore":
                return builder.lessThanOrEqualTo(path, dateValue);
            case "isEmpty":
                return builder.or(builder.isNull(path), builder.equal(path, ""));
            case "isNotEmpty":
                return builder.and(builder.isNotNull(path), builder.notEqual(path, ""));
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }

    public static <T> String[] getAllColumns(Class<T> entityClass) {
        Method[] methods = entityClass.getMethods();
        List<String> columns = new ArrayList<>();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                String fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                columns.add(fieldName);
            }
        }
        return columns.toArray(new String[0]);
    }

    public static <T> Specification<T> search(String value, Class<T> entityClass) {
        Specification<T> spec = Specification.where(null);

        if (value == null || value.equals(""))
            return spec;

        for (String column : getAllColumns(entityClass)) {
            Specification<T> filter = (root, query, builder) -> {
                Path<?> path = root.get(column);
                if (!path.getJavaType().equals(String.class) && !path.getJavaType().isEnum()) {
                    return null;
                }

                Path<String> stringPath = root.get(column);
                return builder.like(builder.lower(stringPath), "%" + value.toLowerCase() + "%");
            };
            spec = spec.or(filter);
        }

        return spec;
    }

/*    public static <T> Specification<T> search(String value, Class<T> entityClass) {
        Specification<T> spec = Specification.where(null);

        for (String column : getAllColumns(entityClass)) {
            Specification<T> filter = (root, query, builder) -> {
                Path<?> path = root.get(column);
                Expression<String> fieldAsString = builder.function("CONVERT", String.class, path);
                return builder.like(builder.lower(column), "%" + value.toLowerCase() + "%");
            };
            spec = spec.or(filter);
            //spec = spec.or(getFilterByField(column, "contains", value));
        }

        return spec;
    }*/
}
