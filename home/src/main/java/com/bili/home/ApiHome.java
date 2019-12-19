package com.bili.home;



import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/19
 * @email: nicech6@163.com
 */
public interface ApiHome {
    /**
     * 首次进入加载（带banner）
     * https://app.bilibili.com/x/feed/index?appkey=1d8b6e7d45233436&build=508000&idx=0&login_event=1&mobi_app=android&network=wifi&open_event=cold&platform=android&pull=true&style=2&ts=1499589051&sign=394a36c0a4fcd89c858e3e77a304aa43
     *
     * 下拉刷新
     * https://app.bilibili.com/x/feed/index?appkey=1d8b6e7d45233436&build=508000&idx=1499589063&login_event=0&mobi_app=android&network=wifi&open_event=&platform=android&pull=true&style=2&ts=1499589096&sign=cbdcc10ed7964205b55b57704d9cebe8
     *
     * 上拉加载
     * https://app.bilibili.com/x/feed/index?appkey=1d8b6e7d45233436&build=509000&idx=1499655142&login_event=0&mobi_app=android&network=wifi&open_event=&platform=android&pull=false&style=2&ts=1499655410&sign=de96086aaa456a817aa69c822f4aa44a
     *
     */
    @GET("/x/feed/index")
    Observable<Object> getIndex(@Query("appkey") String appkey,
                                @Query("build") String build,
                                @Query("idx") int idx,
                                @Query("login_event") int login_event,
                                @Query("mobi_app") String mobi_app,
                                @Query("network") String network,
                                @Query("open_event") String open_event,
                                @Query("platform") String platform,
                                @Query("pull") boolean pull,
                                @Query("style") int style,
                                @Query("ts") String ts
    );
}
