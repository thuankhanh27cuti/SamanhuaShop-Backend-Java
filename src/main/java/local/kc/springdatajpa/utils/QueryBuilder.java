package local.kc.springdatajpa.utils;

import org.springframework.data.domain.Sort;

public class QueryBuilder {
    private QueryBuilder() {}

    public static class Builder {
        private StringBuilder stringBuilder;

        public Builder() {}

        public Builder select(String s) {
            this.stringBuilder = new StringBuilder(s);
            return this;
        }

        public Builder sorted(Sort sort) {
            if (sort.isSorted()) {
                this.stringBuilder.append(" ORDER BY ");
                sort.forEach(order -> {
                    String property = order.getProperty();
                    String direction = order.getDirection().name();
                    this.stringBuilder
                            .append(property)
                            .append(" ")
                            .append(direction)
                            .append(", ");
                });
                this.stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            }
            return this;
        }

        public Builder limit(int start, int offset) {
            this.stringBuilder.append(" LIMIT ").append(start).append(", ").append(offset);
            return this;
        }

        public String build() {
            return this.stringBuilder.toString();
        }
    }
}
