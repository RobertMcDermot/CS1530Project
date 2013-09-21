package com.cs1530.group4.addendum.client;

import java.sql.Date;

import com.cs1530.group4.addendum.shared.Comment;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PostComment extends Composite
{	
	public PostComment(Comment comment)
	{
		FlexTable flexTable = new FlexTable();
		initWidget(flexTable);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(0, 0, horizontalPanel);
		
		Image image = new Image("/addendum/getImage?username="+comment.getUsername());
		image.getElement().getStyle().setProperty("marginRight","10px");
		horizontalPanel.add(image);
		image.setSize("28px", "28px");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		
		Label lblUsername = new Label(comment.getUsername());
		lblUsername.setStyleName("gwt-Label-bold");
		verticalPanel.add(lblUsername);
		
		String formatString = "h:mm a";
		Date now = new Date(System.currentTimeMillis());
		if(comment.getCommentTime().getDate() != now.getDate())
			formatString = "MMM d, yyyy";
		DateTimeFormat dtf = new DateTimeFormat(formatString, new DefaultDateTimeFormatInfo()){};
		Label lblCommenttime = new Label(dtf.format(comment.getCommentTime()));
		lblCommenttime.setStyleName("gwt-Label-grey");
		verticalPanel.add(lblCommenttime);
		
		horizontalPanel.add(verticalPanel);
		
		HTML content = new HTML(comment.getContent());
		flexTable.setWidget(1, 0, content);
	}

}