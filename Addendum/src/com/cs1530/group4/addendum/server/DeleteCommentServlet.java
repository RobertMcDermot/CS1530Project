package com.cs1530.group4.addendum.server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@SuppressWarnings("serial")
public class DeleteCommentServlet extends HttpServlet
{
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		performActions(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		performActions(req, resp);
	}
	
	@SuppressWarnings("unchecked")
	private void performActions(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		Entity comment = getComment(req.getParameter("commentKey"));
		if(comment != null)
		{
			if(comment.hasProperty("attachmentKeys") && comment.getProperty("attachmentKeys") != null)
			{
				for(String key : (ArrayList<String>)comment.getProperty("attachmentKeys"))
				{
					blobstoreService.delete(new BlobKey(key));
				}
			}
			
			memcache.delete(comment.getKey());
			datastore.delete(comment.getKey());
			resp.getWriter().print("done");
		}
		else
			resp.getWriter().print("error");
	}

	private Entity getComment(String commentKey)
	{
		Entity comment = null;
		Key key = KeyFactory.createKey("Comment", Long.valueOf(commentKey).longValue());
		if(memcache.contains(key))
			comment = (Entity) memcache.get(key);
		else
		{
			try
			{
				comment = datastore.get(key);
			}
			catch(EntityNotFoundException ex)
			{
				System.out.println(commentKey + " is an invalid commentKey");
			}
		}

		return comment;
	}
}