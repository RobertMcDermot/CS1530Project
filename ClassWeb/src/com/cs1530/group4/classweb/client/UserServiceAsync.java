/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.cs1530.group4.classweb.client;

import java.util.ArrayList;

import com.cs1530.group4.classweb.shared.Comment;
import com.cs1530.group4.classweb.shared.Course;
import com.cs1530.group4.classweb.shared.Post;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync
{
	void doLogin(String username, String password, AsyncCallback<Boolean> callback);
	void createUser(String username, String password, String firstName, String lastName, AsyncCallback<Boolean> callback);
	void courseSearch(String subjectCode, int catalogueNumber, String courseName, String courseDescription, AsyncCallback<ArrayList<Course>> callback);
	void adminAddCourse(String subjectCode, int catalogueNumber, String courseName, String courseDescription, AsyncCallback<Void> callback);
	void userAddCourse(String username, ArrayList<String> courseIds, AsyncCallback<Void> callback);
	void getUserCourses(String username, AsyncCallback<ArrayList<String>> callback);
	void getPosts(int startIndex, ArrayList<String> streamLevels, AsyncCallback<ArrayList<Post>> callback);
	void uploadPost(String username, String postContent, String streamLevel, AsyncCallback<Void> callback);
	void uploadComment(String postKey, Comment comment, AsyncCallback<Void> callback);
	void upvotePost(String postKey, AsyncCallback<Void> callback);
	void downvotePost(String postKey, AsyncCallback<Void> callback);
}
