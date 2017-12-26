package cn.itcast.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

public class SolrSecond {
	
	private HttpSolrServer solrServer;
	
	@Before
	public void init() {
		// 创建一个solrserver对象
		solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
	}
	
	public void search(SolrQuery query) throws SolrServerException {
		// 执行查询
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("结果的总记录数为 : " + solrDocumentList.getNumFound());
		//高亮显示结果
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println("商品id为 : " + solrDocument.get("id"));
			//获取高亮结果
			String name = "";
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			if (list != null && list.size() > 0) {
				name = list.get(0);
			} else {
				name = (String) solrDocument.get("product_name");
			}
			System.out.println("商品名为 : " + name);
			System.out.println("商品的价格 : " + solrDocument.get("product_price"));
			System.out.println("商品的类名 : " + solrDocument.get("product_catalog_name"));
			System.out.println("商品的照片 : " + solrDocument.get("product_picture"));
		}
	}
	
	@Test
	public void testSolrQuery() throws Exception {
		// 创建一个solrQuery对象
		SolrQuery query = new SolrQuery();
		// 向solrQuer中添加条件
		// 查询全部
		// query.setQuery("*:*");
		// 源生
		query.set("q", "*:*");
		search( query);
	}
	/*
	 * 复杂查询
	 */
	@Test
	public void testQueryFuza() throws Exception {
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("小黄人");
		//设置过滤条件
		query.setFilterQueries("product_catalog_name:幽默杂货","product_price[0 TO 100]");
		//设置排序条件
		query.setSort("product_price",ORDER.asc);
		//分页条件
		query.setStart(0);
		query.setRows(2);
		//设置返回结果中包含的域
		query.setFields("id","product_name","product_catalog_name","product_picture","product_price");
		//设置默认搜索的域
		query.set("df", "product_keywords");
		//开启高亮显示
		query.setHighlight(true);
		//高亮显示的域 
		query.addHighlightField("product_name");
		//前缀
		query.setHighlightSimplePre("<em>");
		//后缀
		query.setHighlightSimplePost("</em>");
		search(query);
	}
	
	

	

}
