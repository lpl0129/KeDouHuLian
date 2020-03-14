package com.qttx.kedouhulian.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qttx.kedouhulian.R;
import com.qttx.kedouhulian.bean.RegionsBean;
import com.qttx.kedouhulian.room.DataBase;
import com.stay.toolslibrary.base.ModuleViewHolder;
import com.stay.toolslibrary.library.nestfulllistview.NestFullGridView;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder;
import com.stay.toolslibrary.library.nicedialog.BaseNiceDialog;
import com.stay.toolslibrary.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author huangyr
 * @date 2018/12/4
 */
public class ChoseCityDilog extends BaseNiceDialog {

    public static ChoseCityDilog newInstance(int heigth, ArrayList<RegionsBean> select, RegionsBean parentCity, int index) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("parent", parentCity);
        bundle.putParcelableArrayList("select", select);
        bundle.putInt("index", index);
        ChoseCityDilog dialog = new ChoseCityDilog();
        dialog.setShowBottom(true);
        dialog.setArguments(bundle);
        dialog.setDimAmount(0);
        dialog.setNeedAni(false);
        dialog.setHeight(heigth);
        return dialog;
    }

    public static ChoseCityDilog newInstance(int heigth, ArrayList<RegionsBean> select, RegionsBean parentCity, int index, int maxsize) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("parent", parentCity);
        bundle.putParcelableArrayList("select", select);
        bundle.putInt("index", index);
        bundle.putInt("maxsize", maxsize);
        ChoseCityDilog dialog = new ChoseCityDilog();
        dialog.setShowBottom(true);
        dialog.setArguments(bundle);
        dialog.setDimAmount(0);
        dialog.setNeedAni(false);
        dialog.setHeight(heigth);
        return dialog;
    }

    /**
     * -1是无全国
     */
    private int index;

    private ArrayList<RegionsBean> select = new ArrayList<>();

    private List<RegionsBean> list = new ArrayList<>();

    private RegionsBean parentCity;
    private NestFullGridView nestselectview;
    private NestFullGridView nestcityview;
    private TextView parentTv;
    private TextView backleveltv;
    private TextView cleartv;

    private Context context;

    private int maxsize = 8;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        maxsize = bundle.getInt("maxsize", 8);
        index = bundle.getInt("index");
        parentCity = bundle.getParcelable("parent");
        if (parentCity == null) {
            parentCity = new RegionsBean();
            parentCity.setLevel(0);
            parentCity.setId(0);
            parentCity.setName("全国");
        }
        ArrayList<RegionsBean> list = bundle.getParcelableArrayList("select");
        select.clear();
        if (list != null) {
            select.addAll(list);
        }

    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        context = c;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_chose_city;
    }


    private void getData() {
        io.reactivex.Observable.create(new ObservableOnSubscribe<List<RegionsBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<RegionsBean>> emitter) throws Exception {
                List<RegionsBean> list =
                        DataBase.getInstance(context)
                                .cityDao()
                                .getCityByPid(parentCity.getId());
                if (index != -1) {
                    list.add(0, parentCity);
                } else if (parentCity.getId() != 0) {
                    //当index=-1时,控制不加入全国
                    list.add(0, parentCity);
                }
                emitter.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Observer<List<RegionsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<RegionsBean> regionsBeans) {
                        list.clear();
                        list.addAll(regionsBeans);
                        nestcityview.updateUI();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 检查是否选中
     *
     * @param bean
     * @return
     */
    public boolean checkSelect(RegionsBean bean) {
        for (RegionsBean item : select) {
            if (item.getId() == bean.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果找到移除选中
     *
     * @param bean
     * @return
     */
    public boolean removeWhenSelect(RegionsBean bean) {
        for (RegionsBean item : select) {
            if (item.getId() == bean.getId()) {
                select.remove(item);
                return true;
            }
        }
        return false;
    }

    /**
     * @param bean
     */
    private void addSelect(RegionsBean bean) {

        //合并规则:替换掉所有的上级和下级
        if (bean.getLevel() == 0) {
            select.clear();
            select.add(bean);
            parentCity = bean;
            nestselectview.updateUI();
            nestcityview.updateUI();
            updateTag();
        } else {
            io.reactivex.Observable.create(new ObservableOnSubscribe<List<Integer>>() {

                @Override
                public void subscribe(ObservableEmitter<List<Integer>> emitter) throws Exception {

                    List<Integer> integers = new ArrayList<>();

                    /**
                     * 移除掉全国数据
                     */
                    integers.add(0);

                    if (bean.getPid() != 0) {
                        //查找上级
                        RegionsBean regionsBean = DataBase.getInstance(context)
                                .cityDao()
                                .getCityByid(bean.getPid());
                        integers.add(regionsBean.getId());

                        if (regionsBean.getPid() != 0) {
                            RegionsBean regionsBean1 = DataBase.getInstance(context)
                                    .cityDao()
                                    .getCityByid(regionsBean.getPid());
                            integers.add(regionsBean1.getId());
                        }
                    }
                    if (bean.getLevel() != 3) {
                        //查找下级
                        List<RegionsBean> regionsBean = DataBase.getInstance(context)
                                .cityDao()
                                .getCityByPid(bean.getId());
                        for (RegionsBean item : regionsBean) {
                            integers.add(item.getId());
                            if (item.getLevel() != 3) {
                                List<RegionsBean> regionsBean2 = DataBase.getInstance(context)
                                        .cityDao()
                                        .getCityByPid(item.getId());
                                for (RegionsBean item2 : regionsBean2) {
                                    integers.add(item2.getId());
                                }
                            }
                        }
                    }
                    emitter.onNext(integers);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(bindToLifecycle())
                    .subscribe(integers -> {
                        int num = select.size() - 1;
                        for (int size = num; size >= 0; size--) {
                            RegionsBean item = select.get(size);
                            if (integers.contains(item.getId())) {
                                select.remove(size);
                            }
                        }
                        if (select.size() < maxsize) {
                            select.add(bean);
                        } else {
                            ToastUtils.showShort("最多选择" + maxsize + "个");
                        }
                        nestselectview.updateUI();
                        nestcityview.updateUI();
                        updateTag();
                    });
        }

    }

    private void updateTag() {
        backleveltv.setVisibility(parentCity.getId() == 0 ? View.GONE : View.VISIBLE);
        parentTv.setText(parentCity.getName());
        cleartv.setVisibility(select.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private onSelectListener onSelectListener;

    public ChoseCityDilog setSelectListener(onSelectListener listener) {
        this.onSelectListener = listener;
        return this;
    }

    @Override
    public void convertView(@NotNull ModuleViewHolder holder, @NotNull BaseNiceDialog dialog) {
        cleartv = holder.findViewById(R.id.clear_select_tv);
        backleveltv = holder.findViewById(R.id.backlevel_tv);
        nestselectview = holder.findViewById(R.id.nestselectview);
        nestcityview = holder.findViewById(R.id.nestcityview);
        parentTv = holder.findViewById(R.id.parent_tv);
        updateTag();

        holder.setOnClickListener(R.id.clear_select_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空
                select.clear();
                updateTag();
                nestselectview.updateUI();
                nestcityview.updateUI();
            }
        });
        holder.setOnClickListener(R.id.sure_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.isEmpty()) {
                    parentCity = null;
                }
                onSelectListener.selectCity(select, parentCity);
                dismiss();
            }
        });
        holder.setOnClickListener(R.id.backlevel_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                io.reactivex.Observable.create(new ObservableOnSubscribe<List<RegionsBean>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<RegionsBean>> emitter) throws Exception {
                        /**
                         * 查找当前parentCity的平级做数据组
                         */
                        List<RegionsBean> list =
                                DataBase.getInstance(context)
                                        .cityDao()
                                        .getCityByPid(parentCity.getPid());

                        /**
                         * 根据当前的平级数据找父级
                         */
                        RegionsBean item = list.get(0);
                        if (item.getPid() == 0) {
                            parentCity = new RegionsBean();
                            parentCity.setId(0);
                            parentCity.setName("全国");
                        } else {
                            parentCity = DataBase.getInstance(context)
                                    .cityDao()
                                    .getCityByid(item.getPid());
                        }
                        list.add(0, parentCity);
                        emitter.onNext(list);
                    }
                })  .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(bindToLifecycle())
                        .subscribe(regionsBeans -> {
                            list.clear();
                            list.addAll(regionsBeans);
                            nestcityview.updateUI();
                            updateTag();
                        });
            }
        });


        nestselectview.setAdapter(new NestFullViewAdapter<RegionsBean>(R.layout.dialog_list_item_chose_city, select) {
            @Override
            public void onBind(int pos, RegionsBean o, NestFullViewHolder holder) {
                TextView text_tv = holder.getView(R.id.text_tv);
                text_tv.setBackgroundResource(R.drawable.attrs_del_tv_bk);
                text_tv.setSelected(true);
                text_tv.setText(o.getName());
            }
        });
        nestselectview.setOnItemClickListener(new NestFullGridView.OnGirdItemClickListener() {
            @Override
            public void onItemClick(NestFullGridView parent, View view, int position) {
                select.remove(position);
                nestselectview.updateUI();
                updateTag();
                nestcityview.updateUI();
            }

            @Override
            public void onLongItemClick(NestFullGridView parent, View view, int position) {

            }
        });
        nestcityview.setAdapter(new NestFullViewAdapter<RegionsBean>(R.layout.dialog_list_item_chose_city, list) {
            @Override
            public void onBind(int pos, RegionsBean o, NestFullViewHolder holder) {
                TextView text_tv = holder.getView(R.id.text_tv);
                text_tv.setSelected(checkSelect(o));
                if (pos == 0) {
                    int level = list.get(pos).getLevel();
                    if (level == 0) {
                        text_tv.setText("全国");
                    } else if (level == 1) {

                        if (index == -1 && parentCity.getId() == 0) {
                            text_tv.setText(o.getName());
                        } else if (o.getName().endsWith("市")) {
                            text_tv.setText("全市");
                        } else {
                            text_tv.setText("全省");
                        }
                    } else if (level == 2) {
                        text_tv.setText("全市");
                    }
                } else {
                    text_tv.setText(o.getName());
                }
            }
        });

        nestcityview.setOnItemClickListener(new NestFullGridView.OnGirdItemClickListener() {
            @Override
            public void onItemClick(NestFullGridView parent, View view, int position) {

                RegionsBean bean = list.get(position);

                if (bean.getId()==parentCity.getId()  || bean.getLevel() == 3) {
                    //选中
                    boolean isselect = removeWhenSelect(list.get(position));
                    if (!isselect) {
                        //未找到
                        addSelect(list.get(position));
                    } else {
                        updateTag();
                        nestselectview.updateUI();
                        nestcityview.updateUI();
                    }
                } else {
                    parentCity = bean;
                    updateTag();
                    getData();
                }


            }

            @Override
            public void onLongItemClick(NestFullGridView parent, View view, int position) {

            }
        });
        getData();
    }

    public interface onSelectListener {
        void selectCity(List<RegionsBean> list, RegionsBean bean);
    }

    private DialogStateListener stateListenerListener;

    public ChoseCityDilog setStateListenerListener(DialogStateListener stateListenerListener) {
        this.stateListenerListener = stateListenerListener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (stateListenerListener != null) {
            stateListenerListener.onDismiss(index);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (stateListenerListener != null) {
            stateListenerListener.onDismiss(index);
        }
    }


}
