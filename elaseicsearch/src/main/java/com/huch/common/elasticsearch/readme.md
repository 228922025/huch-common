


##### 1.为什么要使用ElasticSearch
```
数据库查询有缺点?
数据越大, 查询效率越低;(数据库的解决方案是建立索引, 但是使用前模糊查询,会导致索引失效)
ES可以解决该问题: 解决数据库中数据量过大同时模糊查询会导致数据库索引失效,查询效率低的问题.
```

##### 2.相关概念
```
索引:	(存储数据的位置)
	* 可以看作关系型数据库中的表;
映射:	(存储吗? 存储的类型是什么? 是否分词? 采用的分词器是什么? )
	* 数据如何存储在索引上(为建索引和搜索准备输入文本)
	* 映射mapping (数据如何存放到索引对象上, 需要有一个映射配置, 数据类型, 是否存储, 是否分词.)
文档:	(存储的数据)
	* 就相当于表中的数据	
文档类型: (指定存储文档的类型)
	* 文档是可以有多种类型的
分词器: 
	* IK分词器, 庖丁分词器
词条: (搜索)
```

##### 3.Query对象
```
* matchAllQuery: 	查询索引库中所有的数据;
* termQuery:		词条查询,词条的完全匹配;
* wildcardQuery:	模糊查询(* ? 通配符);
* queryStringQuery:	将查询条件分词后根据词条匹配 	默认返回的并集;
* boolQuery:		must(and), should(or), must_not(not);
```

##### 4.ES的相关操作
```
索引CRUD: ES服务器中维护多个索引库.
映射CRUD: 到底要不要存储? 存储的类型? 要不要分词? 那种分词器?
文档CRUD: ES中存储的数据全部都是json格式的数据.
```

##### 5.Spring整合
```
1. 导入相关jar包;
<!--elasticsearch-->
<dependency>
    <groupId>org.elasticsearch</groupId>
    <artifactId>elasticsearch</artifactId>
    <version>2.4.0</version>
</dependency>
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-elasticsearch</artifactId>
    <version>2.0.4.RELEASE</version>
</dependency>
```

```
<!-- 2. 配置applicationContext.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:elasticsearch="http://www.springframework.org/schema/data/elasticsearch"
       xsi:schemaLocation="
		http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/data/elasticsearch
		http://www.springframework.org/schema/data/elasticsearch/spring-elasticsearch-1.0.xsd">

    <!-- 搜索DAO 扫描 -->
    <elasticsearch:repositories base-package="cn.oranges.index" />

    <!-- 配置Client -->
    <elasticsearch:transport-client id="client" cluster-nodes="127.0.0.1:9300"/>

    <!-- 配置搜索模板  -->
    <bean id="elasticsearchTemplate"
          class="org.springframework.data.elasticsearch.core.ElasticsearchTemplate">
        <constructor-arg name="client" ref="client" />
    </bean>
</beans>
```

```
3.实体类需要添加注解
/**
 * indexName: 索引名称
 * type: 文档的类型
 * @Id: 索引库的id
 * index: 是否进行分词	analyzed:分词	 not_analyzed:不分词  no:不根据此字段进行检索
 * store: 是否存储
 * analyzer: 指定使用的分词器
 * type: 存储数据的类型
 * searchAnalyzer: queryStringQuery就是查询条件进行分词的分词器.
 */
@Document(indexName = "blog3", type = "article")
public class Aticle(){
	@Id
	@Field(index = FieldIndex.not_analyzed, store = true, type = FieldType.Integer)
	private Integer id;
	@Field(index = FieldIndex.analyzed, analyzer = "ik", store = true, searchAnalyzer = "ik", type = FieldType.String)
	private String title;
	@Field(index = FieldIndex.analyzed, analyzer = "ik", store = true, searchAnalyzer = "ik", type = FieldType.String)
	private String content;
    ......
}
```

##### 6.测试数据
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ArticleServiceTest {
	@Autowired
	private ArticleService articleService;

	@Autowired
	private Client client; // 基于原生API

    // 注入模板
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

    // 创建索引和映射
	@Test
	public void createIndex() {
		elasticsearchTemplate.createIndex(Article.class);
		elasticsearchTemplate.putMapping(Article.class);
	}

	@Test
	public void testSave() {
		Article article = new Article();
		article.setId(1001);
		article.setTitle("猪猪侠");
		article.setContent("猪猪侠爱吃棒棒糖");
		articleService.save(article);
	}
}
```

```
//查询
/**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    public Page<WayBill> findAll(Pageable pageable, WayBill wayBill) {
        // 首先判断用户是条件查询还是直接加载所有的数据
        if(StringUtils.isBlank(wayBill.getWayBillNum())
                && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress())
                && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)){  //页面的下拉框会选择0
            // 启动时加载数据
            return wayBillRepository.findAll(pageable);
        }else {
            // 查询条件
            // must 条件必须成立 and
            // must not 条件必须不成立 not
            // should 条件可以成立 or
            BoolQueryBuilder query = new BoolQueryBuilder(); // 布尔查询 ，多条件组合查询
            // 向组合查询对象添加条件
            if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                // 运单号查询
                QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum",
                        wayBill.getWayBillNum());
                query.must(tempQuery);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
                // 发货地 模糊查询
                // 情况一： 输入"北" 是查询词条一部分， 使用模糊匹配词条查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder(
                        "sendAddress", "*" + wayBill.getSendAddress() + "*");

                // 情况二： 输入"北京市海淀区" 是多个词条组合，进行分词后 每个词条匹配查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
                        wayBill.getSendAddress()).field("sendAddress")
                        .defaultOperator(QueryStringQueryBuilder.Operator.AND);

                // 两种情况取or关系
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQueryBuilder);

                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                // 收货地 模糊查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder(
                        "recAddress", "*" + wayBill.getRecAddress() + "*");
                query.must(wildcardQuery);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                // 速运类型 等值查询
                QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
                        wayBill.getSendProNum());
                query.must(termQuery);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                // 速运类型 等值查询
                QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
                        wayBill.getSendProNum());
                query.must(termQuery);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                // 签收状态查询
                QueryBuilder termQuery = new TermQueryBuilder("signStatus",
                        wayBill.getSignStatus());
                query.must(termQuery);
            }

            SearchQuery searchQuery = new NativeSearchQuery(query);
            searchQuery.setPageable(pageable); // 分页效果
            // 有条件查询 、查询索引库
            return wayBillIndexRepository.search(searchQuery);
        }
    }
```

