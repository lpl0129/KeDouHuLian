package com.qttx.kedouhulian.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qttx.kedouhulian.App;
import com.qttx.kedouhulian.R;
import com.qttx.kedouhulian.bean.SealSearchConversationResult;
import com.qttx.kedouhulian.utils.CharacterParser;
import com.qttx.kedouhulian.utils.CommonUtils;
import com.qttx.kedouhulian.utils.UtilsKt;
import com.qttx.kedouhulian.weight.SelectableRoundedImageView;
import com.stay.toolslibrary.base.BaseActivity;
import com.stay.toolslibrary.utils.extension.Glide_ExtensionKt;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.SearchConversationResult;
import io.rong.imlib.model.UserInfo;

/**
 * Created by tiankui on 16/8/31.
 */
public class SealSearchActivity extends BaseActivity {
    private static final int SEARCH_TYPE_FLAG = 1;

    private EditText mSearchEditText;
    private TextView mSearchNoResultsTextView;
    private ImageView mPressBackImageView;
    private LinearLayout mChattingRecordsLinearLayout;
    private LinearLayout mMoreChattingRecordsLinearLayout;
    private ListView mChattingRecordsListView;

    private CharacterParser mCharacterParser;

    private String mFilterString;
    private AsyncTask mAsyncTask;
    private ThreadPoolExecutor mExecutor;
    private List<SearchConversationResult> mSearchConversationResultsList;
    private ArrayList<SearchConversationResult> mSearchConversationResultsArrayList;






    private void initView() {
        mSearchEditText = (EditText) findViewById(R.id.searchEt);
        mSearchNoResultsTextView = (TextView) findViewById(R.id.ac_tv_search_no_results);
        mPressBackImageView = (ImageView) findViewById(R.id.topBack);
        mChattingRecordsLinearLayout = (LinearLayout) findViewById(R.id.ac_ll_filtered_chatting_records_list);
        mMoreChattingRecordsLinearLayout = (LinearLayout) findViewById(R.id.ac_ll_more_chatting_records);
        mChattingRecordsListView = (ListView) findViewById(R.id.ac_lv_filtered_chatting_records_list);

        mChattingRecordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object selectObj = parent.getItemAtPosition(position);
                if (selectObj instanceof SealSearchConversationResult) {
                    SealSearchConversationResult result = (SealSearchConversationResult) selectObj;
                    int count = result.getMatchCount();
                    Conversation conversation = result.getConversation();
                    if (count == 1) {
                        RongIM.getInstance().startConversation(SealSearchActivity.this, conversation.getConversationType(), conversation.getTargetId(), result.getTitle(), result.getConversation().getSentTime());
                    } else {
                        Intent intent = new Intent(SealSearchActivity.this, SealSearchChattingDetailActivity.class);
                        intent.putExtra("filterString", mFilterString);
                        intent.putExtra("searchConversationResult", result);
                        intent.putExtra("flag", SEARCH_TYPE_FLAG);
                        startActivity(intent);
                    }
                }
            }
        });
//        mMoreChattingRecordsLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SealSearchActivity.this, SealSearchMoreChattingRecordsActivity.class);
//                intent.putExtra("filterString", mFilterString);
//                intent.putParcelableArrayListExtra("conversationRecords", mSearchConversationResultsArrayList);
//                startActivity(intent);
//            }
//        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchConversationResultsList = new ArrayList<>();
                mFilterString = s.toString();

                RongIMClient.getInstance().searchConversations(mFilterString,
                        new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP},
                        new String[]{"RC:TxtMsg", "RC:ImgTextMsg", "RC:FileMsg"}, new RongIMClient.ResultCallback<List<SearchConversationResult>>() {
                            @Override
                            public void onSuccess(List<SearchConversationResult> searchConversationResults) {
                                mSearchConversationResultsList = searchConversationResults;
                                mSearchConversationResultsArrayList = new ArrayList<>();
                                for (SearchConversationResult result : searchConversationResults) {
                                    mSearchConversationResultsArrayList.add(result);
                                }
                                if (searchConversationResults.size() > 0) {
                                    mChattingRecordsLinearLayout.setVisibility(View.VISIBLE);
//                                    if (searchConversationResults.size() > 3) {
//                                        mMoreChattingRecordsLinearLayout.setVisibility(View.VISIBLE);
//                                    } else {
//                                        mMoreChattingRecordsLinearLayout.setVisibility(View.GONE);
//                                    }
                                } else {
                                    mChattingRecordsLinearLayout.setVisibility(View.GONE);
                                }
                                if (mFilterString.equals("")) {
                                    mChattingRecordsLinearLayout.setVisibility(View.GONE);
                                    mMoreChattingRecordsLinearLayout.setVisibility(View.GONE);
                                }
                                if ( mSearchConversationResultsList.size() == 0) {
                                    if (mFilterString.equals("")) {
                                        mSearchNoResultsTextView.setVisibility(View.GONE);
                                    } else {
                                        mSearchNoResultsTextView.setVisibility(View.VISIBLE);
                                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                        spannableStringBuilder.append(getResources().getString(R.string.ac_search_no_result_pre));
                                        SpannableStringBuilder colorFilterStr = new SpannableStringBuilder(mFilterString);
                                        colorFilterStr.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), 0, mFilterString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                        spannableStringBuilder.append(colorFilterStr);
                                        spannableStringBuilder.append(getResources().getString(R.string.ac_search_no_result_suffix));
                                        mSearchNoResultsTextView.setText(spannableStringBuilder);
                                    }
                                } else {
                                    mSearchNoResultsTextView.setVisibility(View.GONE);
                                }
                                ChattingRecordsAdapter chattingRecordsAdapter = new ChattingRecordsAdapter(mSearchConversationResultsList);
                                mChattingRecordsListView.setAdapter(chattingRecordsAdapter);

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                if (mFilterString.equals("")) {
                                    mChattingRecordsLinearLayout.setVisibility(View.GONE);
                                    mMoreChattingRecordsLinearLayout.setVisibility(View.GONE);
                                }
                                if ( mSearchConversationResultsList.size() == 0) {
                                    if (mFilterString.equals("")) {
                                        mSearchNoResultsTextView.setVisibility(View.GONE);
                                    } else {
                                        mSearchNoResultsTextView.setVisibility(View.VISIBLE);
                                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                        spannableStringBuilder.append(getResources().getString(R.string.ac_search_no_result_pre));
                                        SpannableStringBuilder colorFilterStr = new SpannableStringBuilder(mFilterString);
                                        colorFilterStr.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), 0, mFilterString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                        spannableStringBuilder.append(colorFilterStr);
                                        spannableStringBuilder.append(getResources().getString(R.string.ac_search_no_result_suffix));
                                        mSearchNoResultsTextView.setText(spannableStringBuilder);
                                    }
                                } else {
                                    mSearchNoResultsTextView.setVisibility(View.GONE);
                                }
                            }
                        });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mPressBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SealSearchActivity.this.finish();
            }
        });
    }

    private void initData() {
        mExecutor = new ThreadPoolExecutor(3, 5, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        mCharacterParser = CharacterParser.getInstance();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_seal_search;
    }

    @Override
    protected void processLogic(@Nullable Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    protected void liveDataListener() {

    }


    class ViewHolder {
        SelectableRoundedImageView portraitImageView;
        LinearLayout nameDisplayNameLinearLayout;
        TextView nameTextView;
        TextView displayNameTextView;
        TextView nameSingleTextView;
    }



    private class ChattingRecordsAdapter extends BaseAdapter {

        private List<SealSearchConversationResult> searchConversationResults;

        public ChattingRecordsAdapter(List<SearchConversationResult> searchConversationResults) {
            this.searchConversationResults = CommonUtils.convertSearchResult(searchConversationResults);
        }

        @Override
        public int getCount() {
            if (searchConversationResults != null) {
//                > 3 ? 3 : searchConversationResults.size();
                return searchConversationResults.size() ;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (searchConversationResults == null)
                return null;

            if (position >= searchConversationResults.size())
                return null;

            return searchConversationResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ChattingRecordsViewHolder viewHolder;
            final SealSearchConversationResult searchResult = (SealSearchConversationResult) getItem(position);
            final Conversation conversation = searchResult.getConversation();
            int searchResultCount = searchResult.getMatchCount();
            if (convertView == null) {
                viewHolder = new ChattingRecordsViewHolder();
                convertView = View.inflate(SealSearchActivity.this, R.layout.item_filter_chatting_records_list, null);
                viewHolder.portraitImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.item_iv_record_image);
                viewHolder.chatDetailLinearLayout = (LinearLayout) convertView.findViewById(R.id.item_ll_chatting_records_detail);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.item_tv_chat_name);
                viewHolder.chatRecordsDetailTextView = (TextView) convertView.findViewById(R.id.item_tv_chatting_records_detail);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ChattingRecordsViewHolder) convertView.getTag();
            }
            if (conversation.getConversationType() == Conversation.ConversationType.PRIVATE) {
                String currentUserId =UtilsKt.getUserId();
               if (conversation.getTargetId().equals(UtilsKt.getUserId())) {
                    searchResult.setId(currentUserId);
                   UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(currentUserId);
                    searchResult.setPortraitUri(userInfo.getPortraitUri());
                    if (!TextUtils.isEmpty(userInfo.getName())) {
                        searchResult.setTitle(userInfo.getName());
                        viewHolder.nameTextView.setText(userInfo.getName());
                    } else {
                        searchResult.setTitle(currentUserId);
                        viewHolder.nameTextView.setText(currentUserId);
                    }
                } else {
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(conversation.getTargetId());
                    searchResult.setPortraitUri(userInfo.getPortraitUri());
//                    ImageLoader.getInstance().displayImage(portraitUri, viewHolder.portraitImageView, App.getOptions());

                   RequestOptions options=new RequestOptions();
                   Glide.with(SealSearchActivity.this).load(userInfo.getPortraitUri()).apply(options).into(viewHolder.portraitImageView);

                    searchResult.setId(conversation.getTargetId());
                    if (userInfo != null) {
                        if (!TextUtils.isEmpty(userInfo.getName())) {
                            searchResult.setTitle(userInfo.getName());
                            viewHolder.nameTextView.setText(userInfo.getName());
                        } else {
                            searchResult.setTitle(userInfo.getUserId());
                            viewHolder.nameTextView.setText(userInfo.getUserId());
                        }
                    } else {
                        searchResult.setId(conversation.getTargetId());
                        searchResult.setTitle(conversation.getTargetId());
                        viewHolder.nameTextView.setText(conversation.getTargetId());
                    }

                }

            }
            if (conversation.getConversationType() == Conversation.ConversationType.GROUP) {
                Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(conversation.getTargetId());
                if (groupInfo != null) {
                    searchResult.setId(groupInfo.getId());

                    searchResult.setPortraitUri(groupInfo.getPortraitUri());
//                    ImageLoader.getInstance().displayImage(portraitUri, viewHolder.portraitImageView, App.getOptions());
                    RequestOptions options=new RequestOptions();
                    Glide.with(SealSearchActivity.this).load(groupInfo.getPortraitUri()).apply(options).into(viewHolder.portraitImageView);
                    if (!TextUtils.isEmpty(groupInfo.getName())) {
                        searchResult.setTitle(groupInfo.getName());
                        viewHolder.nameTextView.setText(groupInfo.getName());
                    } else {
                        searchResult.setTitle(groupInfo.getId());
                        viewHolder.nameTextView.setText(groupInfo.getId());
                    }
                }
            }
            if (searchResultCount == 1) {
                viewHolder.chatRecordsDetailTextView.setText(mCharacterParser.getColoredChattingRecord(mFilterString, searchResult.getConversation().getLatestMessage()));
            } else {
                viewHolder.chatRecordsDetailTextView.setText(getResources().getString(R.string.search_item_chat_records, searchResultCount));
            }
            return convertView;
        }
    }

    class ChattingRecordsViewHolder {
        SelectableRoundedImageView portraitImageView;
        LinearLayout chatDetailLinearLayout;
        TextView nameTextView;
        TextView chatRecordsDetailTextView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        mSearchEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mSearchEditText, 0);
        super.onResume();
    }

    @Override
    protected void onPause() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
        super.onDestroy();
    }
}
