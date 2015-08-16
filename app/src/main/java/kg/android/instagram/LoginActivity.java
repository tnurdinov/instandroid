package kg.android.instagram;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import instagramlogin.InstagramApp;
import instagramlogin.OAuthAuthenticationListener;
import kg.android.instagram.model.Feed;
import kg.android.instagram.model.Media;
import kg.android.instagram.network.RestClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import kg.android.instagram.adapters.InstagramPhotosAdapter;


public class LoginActivity extends Activity {
//    private InstagramPhotosAdapter photosAdapter;
    private InstagramApp mApp;
    private ListView listView;
//    private ArrayList<InstagramPhoto> photos;
    private String MAX_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        listView = (ListView) findViewById(R.id.listView);

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(listener);
        mApp.authorize();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
//                Intent instagramInfo = new Intent(LoginActivity.this, InstagramImageInfoActivity.class);
//                instagramInfo.putExtra("PHOTO", photos.get(position));
//                startActivity(instagramInfo);
//            }
//        });

//        listView.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
//                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
//            }
//        });
//
    }

    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        String userFeedURL = "/users/self/feed?access_token=" + mApp.getAccessToken() + "&max_id=" + MAX_ID;
//        InstagramRestClient.get(userFeedURL, null, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                JSONArray photosJSON = null;
//                try {
//                    photosJSON = response.getJSONArray("data");
//                    MAX_ID = response.getJSONObject("pagination").getString("next_max_id");
//
//                    for (int i = 0; i < photosJSON.length(); i++) {
//                        JSONObject photoJSON = photosJSON.getJSONObject(i);
//                        InstagramPhoto photo = new InstagramPhoto();
//                        photo.username = photoJSON.getJSONObject("user").getString("username");
//                        if (!photoJSON.isNull("caption")){
//                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
//                        }
//                        photo.avatarURL = photoJSON.getJSONObject("user").getString("profile_picture");
//                        photo.imageURL = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
//                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
//
//
//                        photo.comment_list = new ArrayList<Comment>();
//                        JSONArray comments = photoJSON.getJSONObject("comments").getJSONArray("data");
//                        for (int j = 0; j < comments.length(); j++) {
//                            JSONObject commentObj = comments.getJSONObject(j);
//                            Comment comment = new Comment(commentObj.getJSONObject("from").getString("username"), commentObj.getString("text"), commentObj.getJSONObject("from").getString("profile_picture"));
//                            photo.comment_list.add(comment);
//                        }
//                        photos.add(photo);
//                    }
//                    photosAdapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//        });
    }

    private void fetchFeed() {
            RestClient.get().getFeed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Feed>() {
                        @Override
                        public void onCompleted() {
                            Log.d("TAG", "completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onNext(Feed response) {
                            for (Media media : response.getData()) {
                                Log.d("TAG", media.getLink());
                            }
                        }
                    });
    }

    OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {
        @Override
        public void onSuccess() {
            Log.d("Login Item","Success");
            RestClient.setAccessToken(mApp.getAccessToken());
            fetchFeed();
        }

        @Override
        public void onFail(String error) {
            Log.d("Login Item", "Failed");
        }
    };

}
