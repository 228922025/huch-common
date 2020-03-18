package com.huch.common.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.*;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountBuilder;

import java.util.*;

public class GroupBy {
    private SearchRequestBuilder search;
    private String termsName;
    private TermsBuilder termsBuilder;
    private List<Map<String, Object>> subAggList = new ArrayList<>();

    public GroupBy(SearchRequestBuilder search, String termsName, String fieldName, boolean asc) {
        this.search = search;
        this.termsName = termsName;
        termsBuilder = AggregationBuilders.terms(termsName).field(fieldName).order(Terms.Order.term(asc)).size(0);
    }

    private void addSubAggList(String aggName, MetricsAggregationBuilder aggBuilder) {
        Map<String, Object> subAgg = new HashMap<>();
        subAgg.put("aggName", aggName);
        subAgg.put("aggBuilder", aggBuilder);
        subAggList.add(subAgg);
    }

    public void addSumAgg(String aggName, String fieldName) {
        SumBuilder builder = AggregationBuilders.sum(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketSumAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof SumBuilder) {
            tmpMap.put(aggName, bucket.getAggregations().get(aggName).getProperty("value").toString());
            return true;
        } else {
            return false;
        }
    }

    public void addCountAgg(String aggName, String fieldName) {
        ValueCountBuilder builder = AggregationBuilders.count(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketCountAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof ValueCountBuilder) {
            tmpMap.put(aggName, bucket.getAggregations().get(aggName).getProperty("value").toString());
            return true;
        } else {
            return false;
        }
    }

    public void addAvgAgg(String aggName, String fieldName) {
        AvgBuilder builder = AggregationBuilders.avg(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketAvgAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof AvgBuilder) {
            tmpMap.put(aggName, bucket.getAggregations().get(aggName).getProperty("value").toString());
            return true;
        } else {
            return false;
        }
    }

    public void addMinAgg(String aggName, String fieldName) {
        MinBuilder builder = AggregationBuilders.min(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketMinAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof MinBuilder) {
            tmpMap.put(aggName, bucket.getAggregations().get(aggName).getProperty("value").toString());
            return true;
        } else {
            return false;
        }
    }

    public void addMaxAgg(String aggName, String fieldName) {
        MaxBuilder builder = AggregationBuilders.max(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketMaxAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof MaxBuilder) {
            tmpMap.put(aggName, bucket.getAggregations().get(aggName).getProperty("value").toString());
            return true;
        } else {
            return false;
        }
    }

    public void addStatsAgg(String aggName, String fieldName) {
        StatsBuilder builder = AggregationBuilders.stats(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketStatsAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof StatsBuilder) {
            Stats stats = bucket.getAggregations().get(aggName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("min", stats.getMin());
            jsonObject.put("max", stats.getMax());
            jsonObject.put("sum", stats.getMax());
            jsonObject.put("count", stats.getCount());
            jsonObject.put("avg", stats.getAvg());
            tmpMap.put(aggName, jsonObject.toJSONString());
            return true;
        } else {
            return false;
        }
    }

    public void addExtendedStatsAgg(String aggName, String fieldName) {
        ExtendedStatsBuilder builder = AggregationBuilders.extendedStats(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketExtendedStatsAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof ExtendedStatsBuilder) {
            ExtendedStats extendedStats = bucket.getAggregations().get(aggName);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("min", extendedStats.getMin());
            jsonObject.put("max", extendedStats.getMax());
            jsonObject.put("sum", extendedStats.getMax());
            jsonObject.put("count", extendedStats.getCount());
            jsonObject.put("avg", extendedStats.getAvg());
            jsonObject.put("stdDeviation", extendedStats.getStdDeviation());
            jsonObject.put("sumOfSquares", extendedStats.getSumOfSquares());
            jsonObject.put("variance", extendedStats.getVariance());
            tmpMap.put(aggName, jsonObject.toJSONString());
            return true;
        } else {
            return false;
        }
    }

    public void addPercentilesAgg(String aggName, String fieldName) {
        PercentilesBuilder builder = AggregationBuilders.percentiles(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public void addPercentilesAgg(String aggName, String fieldName, double[] percentiles) {
        PercentilesBuilder builder = AggregationBuilders.percentiles(aggName).field(fieldName).percentiles(percentiles);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketPercentilesAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof PercentilesBuilder) {
            Percentiles percentiles = bucket.getAggregations().get(aggName);
            JSONObject jsonObject = new JSONObject();
            for (Percentile percentile : percentiles) {
                jsonObject.put(String.valueOf(percentile.getPercent()), percentile.getValue());
            }
            tmpMap.put(aggName, jsonObject.toJSONString());
            return true;
        } else {
            return false;
        }
    }

    public void addPercentileRanksAgg(String aggName, String fieldName, double[] percentiles) {
        PercentileRanksBuilder builder = AggregationBuilders.percentileRanks(aggName).field(fieldName).percentiles(percentiles);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketPercentileRanksAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof PercentileRanksBuilder) {
            PercentileRanks percentileRanks = bucket.getAggregations().get(aggName);
            JSONObject jsonObject = new JSONObject();
            for (Percentile percentile : percentileRanks) {
                jsonObject.put(String.valueOf(percentile.getPercent()), percentile.getValue());
            }
            tmpMap.put(aggName, jsonObject.toJSONString());
            return true;
        } else {
            return false;
        }
    }

    public void addCardinalityAgg(String aggName, String fieldName) {
        CardinalityBuilder builder = AggregationBuilders.cardinality(aggName).field(fieldName);
        termsBuilder.subAggregation(builder);
        addSubAggList(aggName, builder);
    }

    public boolean bucketCardinalityAgg(Terms.Bucket bucket, String aggName, MetricsAggregationBuilder aggBuilder, Map<String, String> tmpMap) {
        if (aggBuilder instanceof CardinalityBuilder) {
            tmpMap.put(aggName, bucket.getAggregations().get(aggName).getProperty("value").toString());
            return true;
        } else {
            return false;
        }
    }

    public List<Terms.Bucket> getTermsBucket() {
        search.addAggregation(termsBuilder);
        Terms termsGroup = search.get().getAggregations().get(termsName);
        return termsGroup.getBuckets();
    }

    /**
     * 解析所有的子聚合结果
     * @return
     */
    public Map<String, Object> getGroupbyResponse() {
        Map<String, Object> aggResponseMap = new TreeMap<>();
        for (Terms.Bucket bucket : getTermsBucket()) {
            String bucketKeyAsString = bucket.getKeyAsString();
            Map<String, String> tmpMap = new TreeMap<String, String>();
            for (Map<String, Object> subAgg : subAggList) {
                String subAggName = subAgg.get("aggName").toString();
                MetricsAggregationBuilder subAggBuilder = (MetricsAggregationBuilder) subAgg.get("aggBuilder");
                if (bucketAvgAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketMaxAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketMinAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketSumAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketCountAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketCardinalityAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketPercentileRanksAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketPercentilesAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketExtendedStatsAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
                if (bucketStatsAgg(bucket, subAggName, subAggBuilder, tmpMap)) continue;
            }
            aggResponseMap.put(bucketKeyAsString, tmpMap);
        }
        return aggResponseMap;
    }
}
