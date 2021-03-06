package com.cs1530.group4.addendum.client;

import java.util.ArrayList;

import com.cs1530.group4.addendum.shared.Course;
import com.cs1530.group4.addendum.shared.Post;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.client.ProgressBar;

/**
 * This represents the UI for the application's administration page. This is
 * only available to the site's administrator(s)
 */
public class AdminPanel extends Composite
{

	/** The a static instance of the service used for RPC calls. */
	UserServiceAsync userService = UserService.Util.getInstance();

	/**
	 * Instantiates a new AdminPanel.
	 * 
	 * @param main
	 *            a reference to the application's {@link MainView}
	 * 
	 * @custom.accessed None
	 * @custom.changed None
	 * @custom.called None
	 */
	public AdminPanel(final MainView main)
	{
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);

		Anchor logoutAnchor = new Anchor("Logout");
		verticalPanel.add(logoutAnchor);
		logoutAnchor.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				Cookies.removeCookie("loggedIn");
				main.setContent(new Login(main), "login");
			}
		});

		final VerticalPanel courseAddRequests = new VerticalPanel();
		final VerticalPanel reportedPosts = new VerticalPanel();
		final VerticalPanel dbLoadMode = new VerticalPanel();

		final TabPanel tabPanel = new TabPanel();
		verticalPanel.add(tabPanel);
		tabPanel.add(courseAddRequests, "Course Add Requests");
		tabPanel.add(reportedPosts, "Reported Posts");
		tabPanel.add(dbLoadMode, "DB Load Mode");
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>()
		{
			@Override
			public void onSelection(SelectionEvent<Integer> event)
			{
				if(tabPanel.getTabBar().getTabHTML(event.getSelectedItem()).equals("Course Add Requests"))
					getCourseAddRequests(courseAddRequests);
				else if(tabPanel.getTabBar().getTabHTML(event.getSelectedItem()).equals("Reported Posts"))
					getReportedPosts(reportedPosts);
				else if(tabPanel.getTabBar().getTabHTML(event.getSelectedItem()).equals("DB Load Mode"))
					doDbLoadMode(dbLoadMode);
			}
		});

		tabPanel.selectTab(0);
	}

	protected class CancelProgressBarTextFormatter extends ProgressBar.TextFormatter
	{
		@Override
		protected String getText(ProgressBar bar, double curProgress)
		{
			if(curProgress < 0) { return "Cancelled"; }
			return ((int) (100 * bar.getPercent())) + "%";
		}
	}

	private void doDbLoadMode(VerticalPanel dbLoadMode)
	{
		Label textLabel = new Label("Paste the database file to load here");
		final TextArea fileArea = new TextArea();
		fileArea.setSize("700px", "400px");
		Button uploadButton = new Button("Upload");
		uploadButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if(fileArea.getText().length() == 0)
				{
					Window.alert("Please paste user data in first.");
					return;
				}
				
				AsyncCallback<Void> callback = new AsyncCallback<Void>()
				{
					@Override
					public void onFailure(Throwable caught)
					{
						Window.alert("The data is malformed.  Please check the entry and try again.");
					}

					@Override
					public void onSuccess(Void v)
					{
						Window.alert("Users have been added to the database.");
						fileArea.setText("");
					}
				};

				userService.dbLoad(fileArea.getText(), callback);
			}
		});
		
		dbLoadMode.add(textLabel);
		dbLoadMode.add(fileArea);
		dbLoadMode.add(uploadButton);
	}

	/**
	 * Gets the reported posts and displays them in the page's UI
	 * 
	 * @param reportedPosts
	 *            the {@link VerticalPanel} in which to display the posts
	 * 
	 * @custom.accessed None
	 * @custom.changed None
	 * @custom.called {@link com.cs1530.group4.addendum.server.UserServiceImpl#getFlaggedPosts()}
	 */
	private void getReportedPosts(VerticalPanel reportedPosts)
	{
		userService.getFlaggedPosts(new ReportedPostsCallback(reportedPosts));
	}

	/**
	 * Gets the course add requests and displays them in the page's UI.
	 * 
	 * @param courseRequests
	 *            the {@link VerticalPanel} in which to display the requests
	 * 
	 * @custom.accessed None
	 * @custom.changed None
	 * @custom.called {@link com.cs1530.group4.addendum.server.UserServiceImpl#getCourseRequests()}
	 */
	private void getCourseAddRequests(VerticalPanel courseRequests)
	{
		userService.getCourseRequests(new CourseRequestCallback(courseRequests));
	}

	/**
	 * A class encapsulating asynchronous callback methods for the
	 * {@link com.cs1530.group4.addendum.server.UserServiceImpl#getFlaggedPosts()}
	 * method
	 */
	private class ReportedPostsCallback implements AsyncCallback<ArrayList<Post>>
	{

		VerticalPanel postsPanel;
		Button approve, delete;

		public ReportedPostsCallback(VerticalPanel p)
		{
			postsPanel = p;
			postsPanel.clear();
		}

		@Override
		public void onFailure(Throwable caught)
		{}

		@Override
		public void onSuccess(ArrayList<Post> posts)
		{
			for(final Post post : posts)
			{
				final HorizontalPanel row = new HorizontalPanel();
				approve = new Button("Allow Post");
				delete = new Button("Remove Post");
				approve.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						AsyncCallback<Void> callback = new AsyncCallback<Void>()
						{
							@Override
							public void onFailure(Throwable caught)
							{}

							@Override
							public void onSuccess(Void result)
							{
								postsPanel.remove(row);
							}
						};
						userService.flagPost(post.getPostKey(), "", false, callback);
					}
				});
				delete.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						AsyncCallback<Void> callback = new AsyncCallback<Void>()
						{
							@Override
							public void onFailure(Throwable caught)
							{}

							@Override
							public void onSuccess(Void result)
							{
								postsPanel.remove(row);
							}
						};
						userService.deletePost(post.getPostKey(), callback);
					}
				});
				row.add(approve);
				row.add(delete);
				row.add(new UserPost(null, post));
				postsPanel.add(row);
			}
		}
	}

	/**
	 * A class encapsulating asynchronous callback methods for the
	 * {@link com.cs1530.group4.addendum.server.UserServiceImpl#getCourseRequests()}
	 * method
	 */
	private class CourseRequestCallback implements AsyncCallback<ArrayList<Course>>
	{

		VerticalPanel requestsPanel;
		Button approve, delete;

		public CourseRequestCallback(VerticalPanel p)
		{
			requestsPanel = p;
			requestsPanel.clear();
		}

		@Override
		public void onFailure(Throwable caught)
		{}

		@Override
		public void onSuccess(ArrayList<Course> courses)
		{
			for(final Course course : courses)
			{
				final HorizontalPanel row = new HorizontalPanel();
				approve = new Button("Add Course");
				delete = new Button("Delete Request");
				final TextBox subject = new TextBox();
				subject.setText(course.getSubjectCode());
				final IntegerBox number = new IntegerBox();
				number.setValue(course.getCourseNumber());
				final TextBox name = new TextBox();
				name.setText(course.getCourseName());
				final TextBox desc = new TextBox();
				desc.setText(course.getCourseDescription());
				approve.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						course.setSubjectCode(subject.getText());
						course.setCourseNumber(number.getValue());
						course.setCourseName(name.getText());
						course.setCourseDescription(desc.getText());
						AsyncCallback<Void> callback = new AsyncCallback<Void>()
						{
							@Override
							public void onFailure(Throwable caught)
							{}

							@Override
							public void onSuccess(Void result)
							{
								requestsPanel.remove(row);
							}
						};
						userService.removeCourseRequest(course, true, callback);
					}
				});
				delete.addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						AsyncCallback<Void> callback = new AsyncCallback<Void>()
						{
							@Override
							public void onFailure(Throwable caught)
							{}

							@Override
							public void onSuccess(Void result)
							{}
						};
						userService.removeCourseRequest(course, false, callback);
					}
				});
				row.add(approve);
				row.add(delete);
				row.add(new Label("Subject:"));
				row.add(subject);
				row.add(new Label("Number:"));
				row.add(number);
				row.add(new Label("Name:"));
				row.add(name);
				row.add(new Label("Description:"));
				row.add(desc);
				requestsPanel.add(row);
			}
		}
	}
}