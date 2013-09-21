package com.cs1530.group4.addendum.client;

import com.cs1530.group4.addendum.shared.Comment;
import com.cs1530.group4.addendum.shared.Post;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentBox extends Composite
{
	CommentBox commentBox = this;
	UserServiceAsync userService = UserService.Util.getInstance();
	TextArea textArea;
	
	public CommentBox(final PromptedTextBox addComment, final Post post, final UserPost userPost)
	{
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("gwt-DecoratorPanel-newComment");
		verticalPanel.getElement().getStyle().setProperty("padding", "10px");
		initWidget(verticalPanel);
				
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		
		Image image = new Image("contact_picture.png");
		image.getElement().getStyle().setProperty("marginRight", "10px");
		horizontalPanel.add(image);
		image.setSize("28px", "28px");
		
		textArea = new TextArea();
		textArea.addStyleName("small");
		horizontalPanel.add(textArea);
		textArea.setSize("465px", "75px");
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(10);
		verticalPanel.add(horizontalPanel_1);
		
		Button btnSubmit = new Button("Post Comment");
		btnSubmit.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				final Comment comment = new Comment(Cookies.getCookie("loggedIn"),textArea.getText());
				AsyncCallback<Void> callback = new AsyncCallback<Void>()
				{
					@Override
					public void onFailure(Throwable caught){}
					@Override
					public void onSuccess(Void v)
					{
						userPost.addSubmittedComment(comment);
						commentBox.setVisible(false);
						addComment.setVisible(true);
					}
				};
				userService.uploadComment(post.getPostKey(), comment, callback);
			}
		});
		horizontalPanel_1.add(btnSubmit);
		
		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				commentBox.setVisible(false);
				addComment.setVisible(true);
			}		
		});
		horizontalPanel_1.add(btnCancel);
	}
}