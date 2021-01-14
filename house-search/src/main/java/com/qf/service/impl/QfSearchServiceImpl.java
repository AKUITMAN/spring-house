package com.qf.service.impl;

        import com.alibaba.fastjson.JSONObject;
        import com.qf.pojo.resp.BaseResp;
        import com.qf.pojo.resp.HouseResp;
        import com.qf.service.QfSearchService;
        import org.elasticsearch.action.search.SearchRequest;
        import org.elasticsearch.action.search.SearchResponse;
        import org.elasticsearch.client.RestHighLevelClient;
        import org.elasticsearch.common.text.Text;
        import org.elasticsearch.index.query.BoolQueryBuilder;
        import org.elasticsearch.index.query.MultiMatchQueryBuilder;
        import org.elasticsearch.index.query.QueryBuilders;
        import org.elasticsearch.index.query.RangeQueryBuilder;
        import org.elasticsearch.search.SearchHit;
        import org.elasticsearch.search.SearchHits;
        import org.elasticsearch.search.builder.SearchSourceBuilder;
        import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
        import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
        import org.elasticsearch.search.sort.SortOrder;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Service;
        import org.springframework.util.StringUtils;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

/**
 * Created by 54110 on 2020/12/29.
 */
@Service
public class QfSearchServiceImpl implements QfSearchService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Value("${es.index}")
    private String index;
    @Override
    public BaseResp selectKey(String key, Integer page, Integer size, String pricesort) {
        //声明返回值对象
        BaseResp baseResp = new BaseResp();
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (StringUtils.isEmpty(key)){
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }else{
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key,"aHouse","typeName","aName"));
        }

        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("aHouse");
        highlightBuilder.field("aName");

        highlightBuilder.preTags("<font style='color:red'>");
        highlightBuilder.postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //排序判断
//        if (!StringUtils.isEmpty(num)&&num.equals("asc")){
//            searchSourceBuilder.sort("num", SortOrder.ASC);
//        }
//        if (!StringUtils.isEmpty(num)&&num.equals("desc")){
//            searchSourceBuilder.sort("num", SortOrder.DESC);
//        }
        if (!StringUtils.isEmpty(pricesort)&&pricesort.equals("asc")){
            searchSourceBuilder.sort("price", SortOrder.ASC);
        }
        if (!StringUtils.isEmpty(pricesort)&&pricesort.equals("desc")){
            searchSourceBuilder.sort("price", SortOrder.DESC);
        }
        //分页
        int from =(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        //执行请求
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            //解析
            SearchHits hits = search.getHits();
            long totalHits = hits.getTotalHits();
            baseResp.setTotal(totalHits);

            SearchHit[] hits1 = hits.getHits();
            List list = new ArrayList<>();
            for (SearchHit hi:hits1
            ) {
                Map<String, HighlightField> highlightFields = hi.getHighlightFields();

                String ahouse=null;
                String typename=null;
                String aname = null;

                if (highlightFields!=null){
                    HighlightField aHouse = highlightFields.get("aHouse");
                    HighlightField typeName = highlightFields.get("typeName");
                    HighlightField aName = highlightFields.get("aName");

                    if (aHouse!=null){
                        Text[] fragments = aHouse.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                ahouse= stringBuffer.append(te).toString();
                            }
                        }
                    }
                    if (typeName!=null){
                        Text[] fragments = typeName.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                typename= stringBuffer.append(te).toString();
                            }
                        }
                    }

                    if (aName!=null){
                        Text[] fragments = aName.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                aname= stringBuffer.append(te).toString();
                            }
                        }
                    }
                }

                //处理其他数据
                Map<String, Object> sourceAsMap = hi.getSourceAsMap();
                if (ahouse!=null){
                    sourceAsMap.put("aHouse",ahouse);
                }
                if (typename!=null){
                    sourceAsMap.put("typeName",typename);
                }
                if (aname!=null){
                    sourceAsMap.put("aName",aname);
                }

                Object o = JSONObject.toJSON(sourceAsMap);
                HouseResp houseResp = JSONObject.parseObject(o.toString(), HouseResp.class);
                houseResp.setId(Integer.valueOf(hi.getId()));
                list.add(houseResp);
            }
            baseResp.setCode(200);
            baseResp.setMessage("查询成功");
            baseResp.setData(list);
            return baseResp;
        } catch (IOException e) {
            e.printStackTrace();
            baseResp.setCode(30001);
            baseResp.setMessage("查询失败");
            return baseResp;
        }

    }

    @Override
    public BaseResp selectHouse(String key,String check,Double tariff,Integer page, Integer size) {

        //声明返回值对象
        BaseResp baseResp = new BaseResp();
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isEmpty(key)&&StringUtils.isEmpty(check)&&StringUtils.isEmpty(tariff)){
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }else{
            if (StringUtils.isEmpty(check)&&StringUtils.isEmpty(tariff)){
                MultiMatchQueryBuilder muliMatchQuery = QueryBuilders.multiMatchQuery(key, "aHouse","typeName","aName");
                searchSourceBuilder.query(muliMatchQuery);
            }else {
                if (StringUtils.isEmpty(key)&&StringUtils.isEmpty(tariff)){
                    MultiMatchQueryBuilder houseType = QueryBuilders.multiMatchQuery(check, "houseType","orientation","typeName");
                    searchSourceBuilder.query(houseType);
                }else {
                    if (StringUtils.isEmpty(key)&&StringUtils.isEmpty(check)){
                        BoolQueryBuilder filter = boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(tariff+0.0).lte(tariff + 1000.0));
                        searchSourceBuilder.query(filter);
                    }else {
                        if (StringUtils.isEmpty(tariff)){
                            MultiMatchQueryBuilder muliMatchQuery = QueryBuilders.multiMatchQuery(key, "aHouse","typeName","aName");
                            MultiMatchQueryBuilder houseType = QueryBuilders.multiMatchQuery(check, "houseType","orientation","typeName");
                            boolQueryBuilder.must(muliMatchQuery);
                            boolQueryBuilder.must(houseType);
                            searchSourceBuilder.query(boolQueryBuilder);
                        }else {
                            if (StringUtils.isEmpty(key)){
                                MultiMatchQueryBuilder houseType = QueryBuilders.multiMatchQuery(check, "houseType","orientation","typeName");
                                RangeQueryBuilder price = QueryBuilders.rangeQuery("price").gte(tariff).lte(tariff + 1000.0);
                                boolQueryBuilder.must(price);
                                boolQueryBuilder.must(houseType);
                                searchSourceBuilder.query(boolQueryBuilder);
                            }else {
                                if (StringUtils.isEmpty(check)) {
                                    MultiMatchQueryBuilder muliMatchQuery = QueryBuilders.multiMatchQuery(key, "aHouse", "typeName", "aName");
                                    RangeQueryBuilder price = QueryBuilders.rangeQuery("price").gte(tariff).lte(tariff + 1000.0);
                                    boolQueryBuilder.must(price);
                                    boolQueryBuilder.must(muliMatchQuery);
                                    searchSourceBuilder.query(boolQueryBuilder);
                                }else {
                                    MultiMatchQueryBuilder muliMatchQuery = QueryBuilders.multiMatchQuery(key, "aHouse", "typeName", "aName");
                                    RangeQueryBuilder price = QueryBuilders.rangeQuery("price").gte(tariff).lte(tariff + 1000.0);
                                    MultiMatchQueryBuilder houseType = QueryBuilders.multiMatchQuery(check, "houseType","orientation","typeName");
                                    boolQueryBuilder.must(houseType);
                                    boolQueryBuilder.must(price);
                                    boolQueryBuilder.must(muliMatchQuery);
                                    searchSourceBuilder.query(boolQueryBuilder);
                                }
                            }
                        }

                    }

                }

            }
        }

        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("houseType");
        highlightBuilder.field("typeName");
        highlightBuilder.field("orientation");
        highlightBuilder.field("aHouse");
        highlightBuilder.field("aName");
        highlightBuilder.field("price");
        highlightBuilder.preTags("<font style='color:red'>");
        highlightBuilder.postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);

//        highlightBuilder.preTags("<font style='color:red'>");
//        highlightBuilder.postTags("</font>");
//        searchSourceBuilder.highlighter(highlightBuilder);
        //排序判断
//        if (!StringUtils.isEmpty(num)&&num.equals("asc")){
//            searchSourceBuilder.sort("num", SortOrder.ASC);
//        }
//        if (!StringUtils.isEmpty(num)&&num.equals("desc")){
//            searchSourceBuilder.sort("num", SortOrder.DESC);
//        }
//        if (!StringUtils.isEmpty(pricesort)&&pricesort.equals("asc")){
//            searchSourceBuilder.sort("price", SortOrder.ASC);
//        }
//        if (!StringUtils.isEmpty(pricesort)&&pricesort.equals("desc")){
//            searchSourceBuilder.sort("price", SortOrder.DESC);
//        }
        //分页

        int from =(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchRequest.source(searchSourceBuilder);
        //执行请求
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            //解析
            SearchHits hits = search.getHits();
            long totalHits = hits.getTotalHits();
            baseResp.setTotal(totalHits);

            SearchHit[] hits1 = hits.getHits();
            List list = new ArrayList<>();
            for (SearchHit hi:hits1
            ) {
                Map<String, HighlightField> highlightFields = hi.getHighlightFields();

                String housetype=null;
                String orientation1=null;
                String ahouse=null;
                String typename=null;
                String aname = null;
                String price1 = null;


                if (highlightFields!=null){
                    HighlightField houseType = highlightFields.get("houseType");
                    HighlightField aHouse = highlightFields.get("aHouse");
                    HighlightField typeName = highlightFields.get("typeName");
                    HighlightField orientation = highlightFields.get("orientation");
                    HighlightField aName = highlightFields.get("aName");
                    HighlightField price = highlightFields.get("price");

                    if (houseType!=null){
                        Text[] fragments = houseType.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                housetype= stringBuffer.append(te).toString();
                            }
                        }
                    }

                    if (price!=null){
                        Text[] fragments = price.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                price1 = stringBuffer.append(te).toString();
                            }
                        }
                    }

                    if (orientation!=null){
                        Text[] fragments = orientation.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                orientation1 = stringBuffer.append(te).toString();
                            }
                        }
                    }
                    if (aHouse!=null){
                        Text[] fragments = aHouse.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                ahouse= stringBuffer.append(te).toString();
                            }
                        }
                    }
                    if (typeName!=null){
                        Text[] fragments = typeName.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                typename= stringBuffer.append(te).toString();
                            }
                        }
                    }

                    if (aName!=null){
                        Text[] fragments = aName.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        if (fragments!=null){
                            for (Text te:fragments
                            ) {
                                aname= stringBuffer.append(te).toString();
                            }
                        }
                    }

                }

                //处理其他数据
                Map<String, Object> sourceAsMap = hi.getSourceAsMap();
                if (housetype!=null){
                    sourceAsMap.put("houseType",housetype);
                }
                if (orientation1!=null){
                    sourceAsMap.put("orientation",orientation1);
                }
                if (ahouse!=null){
                    sourceAsMap.put("aHouse",ahouse);
                }
                if (typename!=null){
                    sourceAsMap.put("typeName",typename);
                }
                if (aname!=null){
                    sourceAsMap.put("aName",aname);
                }
                if (price1!=null){
                    sourceAsMap.put("price",price1);
                }
                Object o = JSONObject.toJSON(sourceAsMap);
                HouseResp houseResp = JSONObject.parseObject(o.toString(), HouseResp.class);
//                houseResp.setId(Integer.valueOf(hi.getId()));
                list.add(houseResp);
            }
            baseResp.setCode(200);
            baseResp.setMessage("查询成功");
            baseResp.setData(list);
            return baseResp;
        } catch (IOException e) {
            e.printStackTrace();
            baseResp.setCode(30001);
            baseResp.setMessage("查询失败");
            return baseResp;
        }
    }

}
