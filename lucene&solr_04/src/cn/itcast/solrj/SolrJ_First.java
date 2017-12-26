package cn.itcast.solrj;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJ_First {
	
	@Test
	public void addDocument() throws Exception {
		//和solr服务器创建连接
		//参数:solr服务器的地址
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		//第一个参数域的名称.域的名称必须是在 schema.xml 中定义的
		//第二个参数:域的值
		document.addField("id", "c0003");
		document.addField("title", "使用solrJ添加的文档");
		document.addField("content", "文档的内容");
		document.addField("name", "商品名称");
		solrServer.add(document);
		solrServer.commit();
	}
	
	@Test
	public void deleteDocumentById() throws Exception {
		
		//创建连接
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		//根据id删除文档
		solrServer.deleteById("c0003");
		solrServer.commit();
	}
	
	@Test
	public void queryIndex() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询的总个数 : "+solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
		}
		
		
	}
	
	
}
