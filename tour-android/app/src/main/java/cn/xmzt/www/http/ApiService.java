package cn.xmzt.www.http;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.CouponBean;
import cn.xmzt.www.bean.InvoiceTitleBean;
import cn.xmzt.www.bean.OftenVisitorsBean;
import cn.xmzt.www.home.bean.ArticleDetailsBean;
import cn.xmzt.www.home.bean.CityBean;
import cn.xmzt.www.home.bean.CustomizeBean;
import cn.xmzt.www.home.bean.CustomizeForm;
import cn.xmzt.www.home.bean.HomeIndexBean;
import cn.xmzt.www.home.bean.HomeRecommendSubjectBean;
import cn.xmzt.www.home.bean.MustPlayBean;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.intercom.bean.MyTalkGroupsBean;
import cn.xmzt.www.intercom.bean.TalkUserInfoBean;
import cn.xmzt.www.intercom.bean.TourTripListBean;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.mine.bean.AccountSecurityBean;
import cn.xmzt.www.mine.bean.AppVersionBean;
import cn.xmzt.www.mine.bean.BrowseHistoryBean;
import cn.xmzt.www.mine.bean.CashWithdrawalBean;
import cn.xmzt.www.mine.bean.CollectionBean;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.mine.bean.HorseMiMessageBean;
import cn.xmzt.www.mine.bean.HorseMiMessageFilterBean;
import cn.xmzt.www.mine.bean.ImageUrlBean;
import cn.xmzt.www.mine.bean.MessageBean;
import cn.xmzt.www.mine.bean.MessageTypeReadBean;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.mine.bean.MyOrderBean;
import cn.xmzt.www.mine.bean.ShareBean;
import cn.xmzt.www.mine.bean.SignInBean;
import cn.xmzt.www.mine.bean.TourBean;
import cn.xmzt.www.mine.bean.UserInfoBean;
import cn.xmzt.www.mine.bean.UserScoreBean;
import cn.xmzt.www.mine.bean.UserScoreDetailsBean;
import cn.xmzt.www.mine.bean.UserWalletBean;
import cn.xmzt.www.mine.bean.UserWalletDetailsBean;
import cn.xmzt.www.mine.bean.WeatherInfoBean;
import cn.xmzt.www.mine.bean.WxRegisterBean;
import cn.xmzt.www.pay.bean.OrderUnpaid;
import cn.xmzt.www.route.bean.LineOrderForm;
import cn.xmzt.www.route.bean.RouteDetailPage;
import cn.xmzt.www.route.bean.RouteFiltrateList;
import cn.xmzt.www.route.bean.RouteOrderDetailBean;
import cn.xmzt.www.route.bean.RoutePage;
import cn.xmzt.www.route.bean.RoutePreviewBean;
import cn.xmzt.www.route.bean.SearchKeywordBean;
import cn.xmzt.www.route.bean.TimePriceMonthBean;
import cn.xmzt.www.route.bean.TourInsurance;
import cn.xmzt.www.selfdrivingtools.bean.AdvertiseBannerBean;
import cn.xmzt.www.selfdrivingtools.bean.GetDestinationBean;
import cn.xmzt.www.selfdrivingtools.bean.GetTripDaysInfo;
import cn.xmzt.www.selfdrivingtools.bean.GuideMapMyInfo;
import cn.xmzt.www.selfdrivingtools.bean.MsgCarListInfo;
import cn.xmzt.www.selfdrivingtools.bean.MsgGuideListInfo;
import cn.xmzt.www.selfdrivingtools.bean.OfflineFileManagerBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicFeedBackTypeBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicHotSearchBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotDetailBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotGuideBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicVoicePackageBean;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailBean;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;
import cn.xmzt.www.selfdrivingtools.download.DownloadResponseBody;
import cn.xmzt.www.smartteam.bean.SmartTeamInfoBean;
import cn.xmzt.www.smartteam.bean.TripSignInDetailBean;
import cn.xmzt.www.smartteam.bean.TripSignInListBean;
import cn.xmzt.www.ticket.bean.SpecialTicketBean;
import cn.xmzt.www.ticket.bean.SpecialTicketMustPlayBean;
import cn.xmzt.www.ticket.bean.TicketDetailInfo;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {
    public static final String baseUrl = "https://gateway.xmzt.cn/";//正式

    /**
     * 获取验证码
     *
     * @param phone 手机号码
     * @return 验证码信息
     */
    @GET("user/sendValidate")
    Observable<BaseDataBean<Object>> getVerificationCode(@Query("phone") String phone);

    /**
     * 发送安全手机号验证码
     *
     * @return 验证码信息
     */
    @GET("user//user/safePhoneSms")
    Observable<BaseDataBean<String>> getSafePhoneVerificationCode();

    /**
     * 注册
     *
     * @param phone：手机号码
     * @param password：密码
     * @param verificationCode：验证码
     * @return：注册信息
     */
    @FormUrlEncoded
    @POST("user/register/phone")
    Observable<BaseDataBean<Object>> register(@Field("phone") String phone, @Field("password") String password,
                                              @Field("verificationCode") String verificationCode, @Field("client") String client,
                                              @Field("version") String version,@Field("internalNetworkIp") String internalNetworkIp,
                                              @Field("refCode") String refCode);

    /**
     * 密码登录
     *
     * @param phone：手机号码
     * @param password：密码
     * @return：登录信息
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<BaseDataBean<Object>> passwordLogin(@Field("phone") String phone, @Field("password") String password,
                                                   @Field("client") String client, @Field("version") String version);
    /**
     * 微信登录
     *
     * @param code：微信授权码
     */
    @GET("user/wx/open/baseInfo")
    Observable<BaseDataBean<WxRegisterBean>> wxLogin(@Query("code") String code,@Query("refCode") String refCode,@Query("client") String client,@Query("version") String version,@Query("internalNetworkIp") String internalNetworkIp);
    /**
     * 用户登录成功后回传极光rid
     *
     * @param deviceId：rid
     * @param token：密码
     * @return：登录信息
     */
    @GET("user/user/sendJgRid")
    Observable<BaseDataBean<Object>> sendJgRid(@Query("deviceId") String deviceId, @Query("token") String token);

    /**
     * 验证码登录
     *
     * @param phone：手机号码
     * @param verificationCode：验证码
     * @return：登录信息
     */
    @FormUrlEncoded
    @POST("user/login/phone")
    Observable<BaseDataBean<Object>> verificationCodeLogin(@Field("phone") String phone, @Field("verificationCode") String verificationCode,
                                                           @Field("client") String client, @Field("version") String version);

    /**
     * 退出登录
     *
     * @param token：token
     */
    @FormUrlEncoded
    @POST("user/login/out")
    Observable<BaseDataBean<Object>> loginOut(@Field("token") String token);

    /**
     * 获取用户基本信息
     *
     * @param token：密码
     * @return：用户信息
     */
    @GET("user/user/personal")
    Observable<BaseDataBean<UserInfoBean>> getUserInfo(@Query("token") String token);


    /**
     * 找回密码
     *
     * @param phone：手机号码
     * @param password：密码
     * @return：登录信息
     */
    @FormUrlEncoded
    @POST("user/user/password/forget")
    Observable<BaseDataBean<Object>> findPassword(@Field("phone") String phone, @Field("password") String password
            , @Field("verificationCode") String verificationCode);

    /**
     * 修改订单状态为已读
     *
     * @param type：订单类型：1全部 2待支付 3待出行 4退款
     * @return：登录信息
     */
    @FormUrlEncoded
    @POST("tourapi/order/updateReadState")
    Observable<BaseDataBean<Object>> updateReadState(@Query("token") String token, @Field("type") int type);


    /**
     * 修改昵称返回信息
     *
     * @param token：token
     * @param username：新的用户昵称
     * @return
     */
    @FormUrlEncoded
    @POST("user/user/updateUsername")
    Observable<BaseDataBean<Object>> updateUsername(@Field("token") String token, @Field("username") String username);


    /**
     * 获取用户基本信息
     *
     * @param token：token
     * @return 用户基本信息
     */
    @FormUrlEncoded
    @POST("user/user/getUserBasicinfo")
    Observable<BaseDataBean<UserBasicInfoBean>> getUserBasicInfo(@Field("token") String token);

    /**
     * 修改性别
     *
     * @param token：token
     * @param gender：性别   0：保密，1：男 2：女
     * @return 修改后的性别信息
     */
    @FormUrlEncoded
    @POST("user/user/updateGender")
    Observable<BaseDataBean<Object>> modifyGender(@Field("token") String token, @Field("gender") int gender);

    /**
     * 修改年龄
     *
     * @param token：token
     * @param age：年龄
     * @return 修改年龄后的年龄信息
     */
    @FormUrlEncoded
    @POST("user/user/updateAge")
    Observable<BaseDataBean<Object>> modifyAge(@Field("token") String token, @Field("age") int age);

    /**
     * 上传图片，可同时上传多图
     * @param partLis 上传图片，可同时上传多图
     * @param modelName modelName功能模块 common、scenic、user、api
     * @return
     */
    @Multipart
    @POST("resource/open/uplodImgsByFile")
    Observable<BaseDataBean<List<ImageUrlBean>>> uplodImgsByFile(@Part List<MultipartBody.Part> partLis,@Query("modelName") String modelName);
    /**
     * base64上传图片，可同时上传多图，通过参数imgs上传
     * @param imgs 图片数据
     * @param modelName modelName功能模块 common、scenic、user、api
     * @return：上传图片成功后的返回信息
     */
    @FormUrlEncoded
    @POST("resource/open/uplodImgsParam")
    Observable<BaseDataBean<Object>> uploadPicture(@Field("imgs") String imgs,@Query("modelName") String modelName);

    /**
     * 获取我的积分
     *
     * @param token 账户
     * @return 我的积分信息
     */
    @FormUrlEncoded
    @POST("user/sysUserIntegral/getUserIntegral")
    Observable<BaseDataBean<UserScoreBean>> getMyScore(@Field("token") String token);

    /**
     * 获取积分详情信息
     *
     * @param token：token
     * @param pageNum：页码
     * @param pageSize：页数
     * @param type：查询类型：0所有 1收入 2支出
     * @return：积分详情信息
     */
    @FormUrlEncoded
    @POST("user/sysUserIntegralRecord/getUserIntegralRecord")
    Observable<BaseDataBean<List<UserScoreDetailsBean>>> getScoreDetail(@Field("token") String token, @Field("pageNum") int pageNum,
                                                                        @Field("pageSize") int pageSize, @Field("type") int type);

    /**
     * 查询正常用户的账号与安全（校验是否填写信息）
     *
     * @param token 账户
     * @return 查询正常用户的账号与安全（校验是否填写信息）
     */
    @FormUrlEncoded
    @POST("user/user/accountSecurity")
    Observable<BaseDataBean<AccountSecurityBean>> accountSecurity(@Field("token") String token);

    /**
     * 获取积分规则
     *
     * @param type 类型 1=积分规则 2=奖励规则 3=关于我们
     * @return：积分规则
     */
    @FormUrlEncoded
    @POST("tourapi/commonRichtext/getCommonRichtext")
    Observable<BaseDataBean<Object>> getScoreRule(@Field("type") int type);

    /**
     * 获取用户签到信息
     *
     * @param token token
     * @return：签到信息
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserSign/getSignInfo")
    Observable<BaseDataBean<SignInBean>> getSignInInfo(@Field("token") String token);

    /**
     * 获取今日是否签到信息
     *
     * @param userId ：用户id
     * @return 今日是否签到信息
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserSign/userTodayIsSign")
    Observable<BaseDataBean<Object>> getTodaySignInInfo(@Field("userId") int userId); // TODO

    /**
     * 签到
     *
     * @param token ：token
     * @return 签到信息
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserSign/signIn")
    Observable<BaseDataBean<Object>> signIn(@Field("token") String token);

    /**
     * 获取奖励规则
     *
     * @param type 类型 1=积分规则 2=奖励规则 3=关于我们
     * @return：积分规则
     */
    @FormUrlEncoded
    @POST("tourapi/commonRichtext/getCommonRichtext")
    Observable<BaseDataBean<Object>> getRewardRule(@Field("type") int type);

    /**
     * 上传头像
     *
     * @param imgs 图片数据
     * @return：上传图片成功后的返回信息token
     */
    @FormUrlEncoded
    @POST("user/user/updateUserImage")
    Observable<BaseDataBean<Object>> uploadHead(@Field("fileName") String imgs, @Field("token") String token);

    /**
     * 获取我的订单
     *
     * @param token：token
     * @param ordState：订单状态:1待支付,2待出行,3待评价,6已退款（不传默认全部）
     * @param pageNum：当前第几页(默认1，从1开始)
     * @param pageSize：每页大小(默认20)
     * @return 我的订单
     */
    @FormUrlEncoded
    @POST("tourapi/tourOrder/getOrderList")
    Observable<BaseDataBean<List<MyOrderBean>>> getMyOrder(@Field("token") String token, @Field("ordState") int ordState,
                                                           @Field("pageNum") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 取消订单
     */
    @FormUrlEncoded
    @POST("tourapi/tourOrder/cancelOrder")
    Observable<BaseDataBean<Object>> cancelOrder(@Field("token") String token, @Field("id") int id); // TODO

    /**
     * 删除订单
     */
    @FormUrlEncoded
    @POST("tourapi/tourOrder/deleteOrder")
    Observable<BaseDataBean<Object>> deleteOrder(@Field("token") String token, @Field("id") int id); // TODO

    /**
     * 订单申请退款
     */
    @POST("tourapi/order/applyRefund")
    Observable<BaseDataBean<Object>> applyRefund(@Query("token") String token, @Query("orderId") String orderCode);

    /**
     * 获取分享信息
     *
     * @param type：类型 1=链接分析 2=海报分享
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysConfigShare/getSysConfigShare")
    Observable<BaseDataBean<List<ShareBean>>> getShareInfo(@Field("type") int type);

    /**
     * 获取常用出游人
     *
     * @param token：token
     * @param type：1常用出游人 2酒店入住人 3取票人
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserVisitors/getUserIntegral")
    Observable<BaseDataBean<List<TourBean>>> getOftenTourer(@Field("token") String token, @Field("type") int type);

    /**
     * 保存常用出游人
     *
     * @param token：token
     * @param certificateType：证件类型：1身份证
     * @param name：姓名
     * @param tel：手机号
     * @param identityCard：身份证号码
     * @param type：类型：1常用出游人            2酒店入住人 3取票人
     * @return 返回是否成功添加信息
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserVisitors/saveUserIntegral")
    Observable<BaseDataBean<Object>> saveOftenTourer(@Field("token") String token, @Field("certificateType") int certificateType,
                                                     @Field("name") String name, @Field("tel") String tel,
                                                     @Field("identityCard") String identityCard, @Field("type") int type);

    /**
     * 删除常用出游人
     *
     * @param token:token
     * @param id:数据id
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserVisitors/deleteUserIntegral")
    Observable<BaseDataBean<Object>> deleteOftenTourer(@Field("token") String token, @Field("id") int id); // TODO

    /**
     * 编辑常用出游人
     *
     * @param token：token
     * @param id：数据id
     * @param certificateType：证件类型：1身份证
     * @param name：姓名
     * @param tel：手机号
     * @param identityCard：身份证号码
     * @param type：类型：1常用出游人            2酒店入住人 3取票人
     * @return 返回是否成功添加信息
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserVisitors/updateUserIntegral")
    Observable<BaseDataBean<Object>> editOftenTourer(@Field("token") String token, @Field("id") int id,
                                                     @Field("certificateType") int certificateType,
                                                     @Field("name") String name, @Field("tel") String tel,
                                                     @Field("identityCard") String identityCard, @Field("type") int type);


    /**
     * 绑定手机号
     *
     * @param token：token
     * @param phone：手机号码
     * @param verificationCode：验证码
     * @return 返回是否成功绑定信息
     */
    @FormUrlEncoded
    @POST("user/user/phone/bind")
    Observable<BaseDataBean<Object>> bindPhone(@Field("token") String token, @Field("phone") String phone,
                                               @Field("verificationCode") String verificationCode,
                                               @Field("force") boolean force);

    /**
     * 验证原手机号(更换手机号第一步)
     *
     * @param phone：手机号码
     * @param verificationCode：验证码
     * @return
     */
    @FormUrlEncoded
    @POST("user/user/phone/verify")
    Observable<BaseDataBean<Object>> verificationPhone(@Field("phone") String phone, @Field("verificationCode") String verificationCode);

    /**
     * 绑定新手机号(更换手机号第二步)
     *
     * @param operationToken：token
     * @param phone：手机号码
     * @param verificationCode：验证码
     * @return 返回是否成功绑定信息
     */
    @FormUrlEncoded
    @POST("user/user/phone/change")
    Observable<BaseDataBean<Object>> changeBindPhone(@Field("operationToken") String operationToken, @Field("phone") String phone,
                                                     @Field("verificationCode") String verificationCode);

    /**
     * 安全手机号验证码-设置新安全手机号
     *
     * @param operationToken：token
     * @param verificationCode：验证码
     * @return 返回是否成功绑定信息
     */
    @FormUrlEncoded
    @POST("user/user/safePhoneChange")
    Observable<BaseDataBean<Object>> safePhoneChange(@Field("operationToken") String operationToken, @Field("safePhone") String safePhone,
                                                     @Field("verificationCode") String verificationCode);

    /**
     * 修改密码
     *
     * @param token：token
     * @param oldPassword：旧密码
     * @param newPassword：新密码
     * @return 返回是否成功修改密码信息
     */
    @FormUrlEncoded
    @POST("user/user/password/change")
    Observable<BaseDataBean<Object>> modifyPassword(@Field("token") String token, @Field("oldPassword") String oldPassword,
                                                    @Field("newPassword") String newPassword);

    /**
     * 设置密码(无密码设置密码)
     *
     * @param token：token
     * @return 返回是否成功修改密码信息
     */
    @FormUrlEncoded
    @POST("user/user/password/setting")
    Observable<BaseDataBean<Object>> settingPassword(@Field("token") String token, @Field("password") String password);

    /**
     * 设置支付密码（无支付密码时设置）
     *
     * @param token：token
     * @param phone：手机号码
     * @param password：密码
     * @param verificationCode：验证码
     * @return
     */
    @FormUrlEncoded
    @POST("user/user/payPassword/setting")
    Observable<BaseDataBean<Object>> settingPayPassword(@Field("token") String token, @Field("phone") String phone,
                                                        @Field("password") String password, @Field("verificationCode") String verificationCode);


    /**
     * 修改支付密码
     *
     * @param token：token
     * @param phone：手机号码
     * @param password：密码
     * @param verificationCode：验证码
     * @return
     */
    @FormUrlEncoded
    @POST("user/user/payPassword/change")
    Observable<BaseDataBean<Object>> modifyPayPassword(@Field("token") String token, @Field("phone") String phone,
                                                       @Field("password") String password, @Field("verificationCode") String verificationCode);

    /**
     * 安全手机号验证码-校验
     *
     * @param verificationCode：验证码
     * @return
     */
    @FormUrlEncoded
    @POST("user/user/safePhoneSmsCheck")
    Observable<BaseDataBean<String>> safePhoneSmsCheck(@Field("verificationCode") String verificationCode);

    /**
     * 注销检查
     *
     * @param token：token
     * @return 检查注销信息
     */
    @GET("user/user/writeOff/check")
    Observable<BaseDataBean<String>> logoutCheck(@Query("token") String token);

    /**
     * 注销
     *
     * @param token：token
     * @param phone：手机号码
     * @param verificationCode：验证码
     * @return
     */
    @FormUrlEncoded
    @POST("user/user/writeOff")
    Observable<BaseDataBean<Object>> logout(@Field("token") String token, @Field("phone") String phone,
                                            @Field("verificationCode") String verificationCode);

    /**
     * 获取我的资金总额
     *
     * @param token 账户
     * @return 我的资金总额信息
     */
    @FormUrlEncoded
    @POST("user/sysUserCapital/getSysUserCapital")
    Observable<BaseDataBean<UserWalletBean>> getMoneySum(@Field("token") String token);

    /**
     * 获当前用户的所有提现账号(提现剩余次数以及提现最小金额)
     *
     * @param token 账户
     * @return 我的资金总额信息
     */
    @FormUrlEncoded
    @POST("user/sysUserExtractionAccount/getByUserId")
    Observable<BaseDataBean<CashWithdrawalBean>> getCashAccount(@Field("token") String token);


    /**
     * 修改提现账号
     *
     * @param token 账户
     * @return 我的资金总额信息
     */
    @FormUrlEncoded
    @POST("user/sysUserExtractionAccount/update")
    Observable<BaseDataBean<Object>> updateCashAccount(@Field("token") String token, @Field("account") String account, @Field("extractTel") String extractTel
            , @Field("id") int id, @Field("realName") String realName, @Field("type") int type, @Field("verificationCode") String verificationCode);

    /**
     * 添加提现账号
     *
     * @param token 账户
     * @return 我的资金总额信息
     */
    @FormUrlEncoded
    @POST("user/sysUserExtractionAccount/save")
    Observable<BaseDataBean<Object>> saveCashAccount(@Field("token") String token, @Field("account") String account, @Field("extractTel") String extractTel
            , @Field("realName") String realName, @Field("type") int type, @Field("verificationCode") String verificationCode);

    /**
     * 用户申请提现（冻结动作）
     *
     * @param token 账户
     * @return 我的资金总额信息
     */
    @FormUrlEncoded
    @POST("user/sysUserCapital/saveSysUserCapitalExtraction")
    Observable<BaseDataBean<Object>> cash(@Field("token") String token, @Field("extractionAccountId") int extractionAccountId, @Field("moneyNum") double moneyNum);

    /**
     * 获取我的资金流水信息
     *
     * @param token：token
     * @param pageNum：页码
     * @param pageSize：页数
     * @param type：查询类型：0所有 1收入 2支出
     * @return：积分详情信息
     */
    @FormUrlEncoded
    @POST("user/sysUserCapital/getSysUserCapitalRecord")
    Observable<BaseDataBean<List<UserWalletDetailsBean>>> getBill(@Field("token") String token, @Field("pageNum") int pageNum,
                                                                  @Field("pageSize") int pageSize, @Field("type") int type);


    /**
     * 获取我的收藏信息
     *
     * @param token：token
     * @param type：收藏类型：1酒店 2线路 3门票
     * @param pageNum：页码
     * @param pageSize：页数
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserCollection/get")
    Observable<BaseDataBean<List<CollectionBean>>> getCollectionInfo(@Field("token") String token, @Field("type") int type, @Field("pageNum") int pageNum,
                                                                     @Field("pageSize") int pageSize);

    /**
     * 取消收藏
     *
     * @param token：token
     * @param ids：需要取消收藏的数据
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserCollection/delete")
    Observable<BaseDataBean<Object>> cancelCollection(@Field("token") String token, @Field("ids") String ids);

    /**
     * 添加收藏信息
     *
     * @param token：token
     * @param dataid：dataid
     * @param type：type     收藏类型：0全部 1酒店 2线路 3门票
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserCollection/save")
    Observable<BaseDataBean<Long>> addCollectionInfo(@Field("token") String token, @Field("dataid") int dataid, @Field("type") int type);

    /**
     * 获取我的历史记录
     *
     * @param token：token
     * @param type：收藏类型：1酒店 2线路 3门票
     * @param pageNum：页码
     * @param pageSize：页数
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserHistory/get")
    Observable<BaseDataBean<List<BrowseHistoryBean>>> getHistoryRecord(@Field("token") String token, @Field("type") int type, @Field("pageNum") int pageNum,

                                                                       @Field("pageSize") int pageSize);

    /**
     * 删除历史
     *
     * @param token：token
     * @param ids：需要取消收藏的数据
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserHistory/delete")
    Observable<BaseDataBean<Object>> deleteHistory(@Field("token") String token, @Field("ids") String ids);

    /**
     * 保存意见反馈
     *
     * @param token：token
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysFeedback/saveFeedback")
    Observable<BaseDataBean<Object>> saveFeedback(@Field("text") String text, @Field("token") String token, @Field("type") Integer type);

    /**
     * post Body请求
     *
     * @param body
     * @return
     */
    @POST("{url}")
    Observable<BaseDataBean<String>> postBody(@Path(value = "url", encoded = true) String url, @Body Map<String, Object> body);

    /**
     * 筛选条件列表
     *
     * @return
     */
    @GET("tourapi/line/filtrateList")
    Observable<BaseDataBean<RouteFiltrateList>> filtrateList();

    /**
     * 搜索推荐关键字列表
     *
     * @param type     类型(1酒店 2线路 3门票 4景区)，多个使用逗号隔开
     * @param citycode 城市
     * @param limit    返回条数
     * @return
     */
    @GET("tourapi/search/recommend")
    Observable<BaseDataBean<List<SearchKeywordBean>>> recommendSearchKeywordList(@Query("type") String type, @Query("citycode") String citycode, @Query("limit") int limit);

    /**
     * 搜索推荐关键字列表
     *
     * @param type     数据类型：1酒店 2线路 3景区门票 4景区工具类
     * @param cityName 城市
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("tourapi/search/hotSearchKeyword")
    Observable<BaseDataBean<List<SearchKeywordBean>>> hotSearchKeywordList(@Query("type") String type, @Query("cityName") String cityName, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 关键字列表
     *
     * @return
     */
    @GET("tourapi/line/keywords")
    Observable<BaseDataBean<String>> lineKeywords(@Query("keyword") String keyword, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 线路搜索列表
     *
     * @param arrivalCity  目的城市
     * @param city         起始城市
     * @param daySize      日期天数
     * @param departDate   出发时间yyyy-MM
     * @param discounts    是否特惠推荐(0:否,1:是)
     * @param guessLike    是否猜你喜欢(0:否,1:错峰出行,2:亲子时光,3:休闲度假)
     * @param isTactics    是否有领队(0:没有,1有)
     * @param keyword      关键字
     * @param locationCity 当前定位城市
     * @param pageNum      当前第几页(默认1，从1开始)
     * @param pageSize     每页大小(默认20)
     * @param popPlay      是否人气必玩(0:否,1:是)
     * @param purchase     是否超值抢购(0:否,1:是)
     * @param sortType     排序 (1:销量优先,2:价格从低到高,3:价格从高到低)
     * @param theme        主题code，多个用英文逗号隔开
     * @return
     */
    @GET("tourapi/line/search")
    Observable<BaseDataBean<RoutePage>> lineSearch(@Query("arrivalCity") String arrivalCity,
                                                   @Query("city") String city,
                                                   @Query("daySize") String daySize,
                                                   @Query("departDate") String departDate,
                                                   @Query("discounts") String discounts,
                                                   @Query("guessLike") String guessLike,
                                                   @Query("isTactics") String isTactics,
                                                   @Query("keyword") String keyword,
                                                   @Query("locationCity") String locationCity,
                                                   @Query("pageNum") int pageNum,
                                                   @Query("pageSize") int pageSize,
                                                   @Query("popPlay") String popPlay,
                                                   @Query("purchase") String purchase,
                                                   @Query("sortType") String sortType,
                                                   @Query("theme") String theme
    );

    /**
     * 线路详情推荐列表
     *
     * @return
     */
    @GET("tourapi/line/recommends")
    Observable<BaseDataBean<String>> lineRecommends();

    /**
     * 获取线路详情
     *
     * @param token：token
     * @param lineId：线路id
     * @return
     */
    @GET("tourapi/line/detail")
    Observable<BaseDataBean<RouteDetailPage>> getRouteDetail(@Query("token") String token, @Query("lineId") int lineId, @Query("isNewRoute") boolean isNewRoute);

    /**
     * 特价门票
     *
     * @param location 用户当前的经纬度位置，英文逗号连接
     * @return
     */
    @GET("tourapi/ticket/home")
    Observable<BaseDataBean<SpecialTicketBean>> getSpecialTicketHome(@Query("location") String location);

    /**
     * 人气必玩
     *
     * @param city 城市
     * @return
     */
    @GET("tourapi/ticket/popularityPlay")
    Observable<BaseDataBean<SpecialTicketMustPlayBean>> getSpecialTicketMustPlay(@Query("city") String city);

    /**
     * 门票详请
     *
     * @param id 门票id
     * @return
     */
    @GET("tourapi/ticket/scenic")
    Observable<BaseDataBean<TicketDetailInfo>> getTicketDetail(@Query("id") int id);

    /**
     * 线路日历价格
     *
     * @return
     */
    @GET("tourapi/line/prices")
    Observable<Response<BaseDataBean<List<TimePriceMonthBean>>>> getLineTimePrices(@Query("lineId") int lineId, @Query("months") int months);

    /**
     * 景区列表搜索
     *
     * @param city     当前定位城市
     * @param keyword  搜索关键字
     * @param location 经纬度，用英文逗号隔开
     * @param pageNum  当前第几页（默认1，从1开始）
     * @param pageSize 每页大小（默认20）
     * @return
     */
    @GET("scenic/search")
    Observable<BaseDataBean<WisdomGuideInfo>> getWisdomGuideList(
            @Query("city") String city,
            @Query("keyword") String keyword,
            @Query("location") String location,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize
    );

    /**
     * 景区导览信息（电子地图导览接口）
     * 包含景区，景点列表，线路列表，服务点列表
     *
     * @param id        景区id
     * @param latitude  纬度
     * @param longitude 经度
     * @return
     */
    @GET("scenic/guide")
    Observable<BaseDataBean<ScenicSpotGuideBean>> getScenicSpotGuideInfo(
            @Query("id") long id,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
    );

    /**
     * 创建线路订单
     *
     * @return
     */
    @POST("tourapi/order/line")
    Observable<BaseDataBean<String>> postOrderLine(@Body LineOrderForm orderForm);
    /**
     * 订单-获取线路的保险信息
     *
     * @return
     */
    @GET("tourapi/order/getInsuranceInfo")
    Observable<BaseDataBean<TourInsurance>> getInsuranceInfo(@Query("lineId") int lineId);


    /**
     * 待支付订单(选择支付页面的数据)
     *
     * @param orderCode
     * @param token
     * @return
     */
    @GET("tourapi/order/unpaid")
    Observable<BaseDataBean<OrderUnpaid>> getOrderUnpaid(@Query("orderId") String orderCode, @Query("token") String token);

    /**
     * 预下单(获取第三方支付信息)
     *
     * @param code      公众号支付时的用户code
     * @param orderCode 订单id编码
     * @param source    来源：1App 2公众号
     * @param type      下单类型：1微信 2支付宝
     * @param token
     * @return
     */
    @POST("tourapi/order/placeOrder")
    Observable<BaseDataBean<Object>> postPlaceOrder(@Query("code") String code,
                                                    @Query("orderId") String orderCode,
                                                    @Query("source") int source,
                                                    @Query("type") int type,
                                                    @Query("token") String token);

    /**
     * 获取首页Index
     *
     * @param cityCode 城市code
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/home/index")
    Observable<BaseDataBean<HomeIndexBean>> postHomeIndex(@Field("cityCode") int cityCode);

    /**
     * 获取首页Index(新版)
     *
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/home/newIndex")
    Observable<BaseDataBean<HomeIndexBean>> postHomeNewIndex(@Field("token") String token);

    /**
     * 获取首页主题推荐
     *
     * @param showLocation 展示位置(0:App首页，1:线路首页, 2:酒店首页, 3:门票首页)
     * @return
     */
    @GET("tourapi/home/recommend/subject")
    Observable<BaseDataBean<List<HomeRecommendSubjectBean>>> getHomeRecommendSubject(@Query("showLocation") int showLocation);

    /**
     * 获取行程列表
     *
     * @param token           token
     * @param currentCityCode 定位城市的code
     * @param isFinish        是否查询结束的行程：0否 1是
     * @param pageNum         当前第几页(默认1，从1开始)
     * @param pageSize        每页大小(默认20)
     * @return
     */
    @GET("tourapi/tourTrip/tripList")
    Observable<BaseDataBean<TourTripListBean>> getTourTripList(@Query("token") String token,
                                                               @Query("currentCityCode") String currentCityCode,
                                                               @Query("isFinish") int isFinish,
                                                               @Query("pageNum") int pageNum,
                                                               @Query("pageSize") Integer pageSize);

    /**
     * 删除行程
     *
     * @param ids 行程id，多个id用英文逗号隔开
     * @return
     */
    @GET("tourapi/tourTrip/deleteTrip")
    Observable<BaseDataBean<Object>> deleteTrip(@Query("ids") String ids);

    /**
     * 获取对讲云信账号信息
     *
     * @param token：token
     * @return：云信信息
     */
    @GET("talkback/user/account")
    Observable<BaseDataBean<TalkUserInfoBean>> getTalkUserAccount(@Query("token") String token);

    /**
     * 获取我的群列表
     *
     * @param token：token
     * @return：云信信息
     */
    @GET("talkback/group/list")
    Observable<BaseDataBean<List<MyTalkGroupBean>>> getTalkGroupList(@Query("token") String token);
    /**
     * 删除行程群
     * @param token
     * @param groups 行程群ids，多个id用英文逗号隔开
     * @return
     */
    @POST("talkback/group/delete")
    Observable<BaseDataBean<Object>> deleteGroup(@Query("token") String token,@Query("groups") String groups);

    /**
     * 获取常用出游人
     *
     * @param token：token
     * @param type：1常用出游人 2酒店入住人 3取票人
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/sysUserVisitors/getUserIntegral")
    Observable<BaseDataBean<List<OftenVisitorsBean>>> getOftenVisitors(@Field("token") String token, @Field("type") int type);

    /**
     * 景区搜索通过关键字
     *
     * @param city     当前定位城市
     * @param keyword  搜索关键字
     * @param location 经纬度，用英文逗号隔开
     * @param pageNum  当前第几页（默认1，从1开始）
     * @param pageSize 每页大小（默认20）
     * @return
     */
    @GET("scenic/keywords")
    Observable<BaseDataBean<WisdomGuideInfo>> getKeyWordsSearchList(
            @Query("city") String city,
            @Query("keyword") String keyword,
            @Query("location") String location,
            @Query("pageNum") int pageNum,
            @Query("pageSize") int pageSize
    );

    /**
     * 景区热门搜索
     *
     * @param limit 返回条数
     * @param type  类型(1: 城市 11: 景区)
     * @return
     */
    @GET("tourapi/search/hot")
    Observable<BaseDataBean<List<ScenicHotSearchBean>>> getHotSearchList(
            @Query("limit") int limit,
            @Query("type") int type
    );

    /**
     * 优惠券列表
     *
     * @param type 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
     * @return
     */
    @GET("tourapi/coupon/list")
    Observable<BaseDataBean<List<CouponBean>>> getCouponList(@Query("type") int type);

    /**
     * 领取优惠劵
     *
     * @param token    用户token
     * @param couponId 优惠券Id
     * @return
     */
    @POST("tourapi/user/coupon/getCoupon")
    Observable<BaseDataBean<String>> getCoupon(@Query("token") String token, @Query("couponId") int couponId);

    /**
     * 添加或删除文章点赞信息
     *
     * @param token     用户token
     * @param articleId
     * @return
     */
    @POST("tourapi/tourArticle/addOrDeleteById")
    Observable<BaseDataBean<Object>> addOrDeleteById(@Query("token") String token, @Query("articleId") int articleId);

    /**
     * 微信登录
     *
     * @return
     */
    @POST("user/wxRegister/android")
    Observable<BaseDataBean<WxRegisterBean>> wxRegister(@Query("nickname") String nickname, @Query("openId") String openId
            , @Query("unionId") String unionId, @Query("headImgUrl") String headImgUrl, @Query("city") String city);

    /**
     * 我的优惠券列表
     *
     * @param sortType 排序方式(1: 领取时间倒序，2：到期时间顺序)
     * @param state    优惠券状态(0:未使用,1:已使用,2:已过期)
     * @param token    用户token
     * @param type     优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
     * @return
     */
    @GET("tourapi/user/coupon")
    Observable<BaseDataBean<List<MyCouponBean>>> getMyCouponList(@Query("sortType") int sortType,
                                                                 @Query("state") int state,
                                                                 @Query("token") String token,
                                                                 @Query("type") String type);

    /**
     * 发票抬头列表
     *
     * @param token 用户token
     * @return
     */
    @POST("tourapi/tourInvoiceTitle/invoiceTitleList")
    Observable<BaseDataBean<List<InvoiceTitleBean>>> getInvoiceTitleList(@Query("token") String token);

    /**
     * 添加发票抬头
     *
     * @param bankCount           银行账号
     * @param depositBank         开户银行
     * @param dutyParagraph       税号
     * @param registrationAddress 注册地址
     * @param registrationPhone   注册电话
     * @param titleName           抬头名称
     * @param titleType           抬头类型(1:公司,2:个人)
     * @param token               用户token
     * @return
     */
    @POST("tourapi/tourInvoiceTitle/addInvoiceTitle")
    Observable<BaseDataBean<String>> addInvoiceTitle(@Query("bankCount") String bankCount,
                                                     @Query("depositBank") String depositBank,
                                                     @Query("dutyParagraph") String dutyParagraph,
                                                     @Query("registrationAddress") String registrationAddress,
                                                     @Query("registrationPhone") String registrationPhone,
                                                     @Query("titleName") String titleName,
                                                     @Query("titleType") int titleType,
                                                     @Query("token") String token);

    /**
     * 更新发票抬头
     *
     * @param bankCount           银行账号
     * @param depositBank         开户银行
     * @param dutyParagraph       税号
     * @param id                  发票抬头ID
     * @param registrationAddress 注册地址
     * @param registrationPhone   注册电话
     * @param titleName           抬头名称
     * @param titleType           抬头类型(1:公司,2:个人)
     * @param token               用户token
     * @return
     */
    @POST("tourapi/tourInvoiceTitle/updateInvoiceTitle")
    Observable<BaseDataBean<String>> updateInvoiceTitle(@Query("bankCount") String bankCount,
                                                        @Query("depositBank") String depositBank,
                                                        @Query("dutyParagraph") String dutyParagraph,
                                                        @Query("id") int id,
                                                        @Query("registrationAddress") String registrationAddress,
                                                        @Query("registrationPhone") String registrationPhone,
                                                        @Query("titleName") String titleName,
                                                        @Query("titleType") int titleType,
                                                        @Query("token") String token);

    /**
     * 删除发票抬头
     *
     * @param id
     * @param token 用户token
     * @return
     */
    @POST("tourapi/tourInvoiceTitle/deleteInvoiceTitle")
    Observable<BaseDataBean<String>> deleteInvoiceTitle(@Query("id") int id, @Query("token") String token);

    /**
     * 景点离线包下载
     *
     * @param id 景区编号id
     * @return
     */
    @GET("scenic/officeline/packages")
    Observable<BaseDataBean<ScenicVoicePackageBean>> getScenicVoicePackage(@Query("id") long id);

    /**
     * 断点续传下载接口
     *
     * @param range 传0表示从头开始下载
     * @param url   下载文件的地址
     * @return
     */
    @Streaming /*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<DownloadResponseBody> executeDownload(@Header("Range") String range, @Url() String url);

    /**
     * 线路预览
     *
     * @param lineId
     * @return
     */
    @GET("tourapi/line/map/preview")
    Observable<BaseDataBean<RoutePreviewBean>> getRoutePreview(@Query("lineId") int lineId);

    /**
     * 订单详情
     *
     * @param orderId
     * @param token
     * @return
     */
    @GET("tourapi/order/detail")
    Observable<BaseDataBean<RouteOrderDetailBean>> getOrderDetail(@Query("orderId") String orderId, @Query("token") String token);

    /**
     *添加发票
     * @param orderCode
     * @param invoiceTitleId
     * @param itemTitle
     * @param receiveEmail
     * @return
     */
    @POST("tourapi/invoice/save")
    Observable<BaseDataBean<String>> addInvoice(@Query("orderId") String orderCode,
                                                @Query("invoiceTitleId") int invoiceTitleId,
                                                @Query("itemTitle") String itemTitle,
                                                 @Query("receiveEmail") String receiveEmail);
    /**
     * 重新发送电子发票
     *
     * @param orderCode 订单编号
     * @param email     邮箱
     */
    @POST("tourapi/invoice/send")
    Observable<BaseDataBean<String>> sendInvoice(@Query("orderId") String orderCode,
                                                 @Query("email") String email, @Query("token") String token);

    /**
     * 景区 意见反馈-问题类型
     *
     * @return
     */
    @GET("scenic/feedback/question/types")
    Observable<BaseDataBean<List<ScenicFeedBackTypeBean>>> getScenicFeedBack();

    /**
     * 景区 意见反馈-保存
     *
     * @param bigClass    问题大类
     * @param description 补充描述
     * @param phone       手机号
     * @param scenicId    景区id
     * @param smallClass  问题小类
     * @param token       用户登录token
     * @return
     */
    @POST("scenic/feedback")
    Observable<BaseDataBean<String>> getScenicFeedBackSave(@Query("bigClass") String bigClass,
                                                           @Query("description") String description,
                                                           @Query("phone") String phone,
                                                           @Query("scenicId") long scenicId,
                                                           @Query("smallClass") String smallClass,
                                                           @Query("token") String token);

    /**
     * 广告列表(轮播图列表)
     *
     * @param type 类型0:首页,1:景区,2:酒店, 3:线路, 4:门票
     * @return
     */
    @GET("tourapi/advertise")
    Observable<BaseDataBean<List<AdvertiseBannerBean>>> getAdvertiseBanner(@Query("type") int type);

    /**
     * 景点详请
     *
     * @param id 景点id
     * @return
     */
    @GET("scenic/spot")
    Observable<BaseDataBean<ScenicSpotDetailBean>> getScenicSpotData(@Query("id") int id);

    /**
     * 播放操作
     *
     * @param id 景区id
     * @return
     */
    @GET("scenic/playOperation")
    Observable<BaseDataBean<String>> getPlayOperation(@Query("id") long id);


    /**
     * 人气必玩（卡片）
     *
     * @param citycode 城市dode
     * @param pageNum
     * @param pageSize
     * @param type     类型：1出发地 2目的地
     * @return
     */
    @FormUrlEncoded
    @POST("tourapi/home/getPopularityList")
    Observable<BaseDataBean<List<MustPlayBean>>> getPopularityList(@Field("citycode") String citycode, @Field("pageNum") int pageNum,
                                                                   @Field("pageSize") int pageSize, @Field("type") int type);

    /**
     * 获取文章详情
     *
     * @param id 文章
     * @return
     */
    @GET("tourapi/tourArticle/queryById")
    Observable<BaseDataBean<ArticleDetailsBean>> queryById(@Query("token") String token, @Query("id") int id);

    /**
     * 添加私人订制
     *
     * @param token       用户token
     * @param departArea  出发地，数据格式：[{’citycode’:’110000’,’cityname’:’北京市’}]
     * @param arrivalArea 目的地，数据格式：[{’citycode’:’110000’,’cityname’:’北京市’}]
     * @param departDate  出发时间
     * @param tel         联系人电话
     * @param wechat      微信号
     * @param budget      人均预算
     * @param crNumber    出行成人数
     * @param xhNumber    出行儿童数
     * @return
     */
    @POST("tourapi/tourCustomize/save")
    Observable<BaseDataBean<Object>> addCustomize(@Query("token") String token,
                                                  @Query("departArea") String departArea,
                                                  @Query("arrivalArea") String arrivalArea,
                                                  @Query("departDate") String departDate,
                                                  @Query("tel") String tel,
                                                  @Query("wechat") String wechat,
                                                  @Query("budget") double budget,
                                                  @Query("crNumber") Integer crNumber,
                                                  @Query("xhNumber") Integer xhNumber);
    /**
     *  添加或修改私人订制（新版）
     * @param token       用户token
     * @param mCustomizeForm  表单
     * @return
     */
    @POST("tourapi/tourCustomize/newSave")
    Observable<BaseDataBean<Object>> addNewCustomize(@Query("token") String token,@Body CustomizeForm mCustomizeForm );

    /**
     * 修改私人订制
     *
     * @param token       用户token
     * @param id          私人订制id
     * @param departArea  出发地，数据格式：[{’citycode’:’110000’,’cityname’:’北京市’}]
     * @param arrivalArea 目的地，数据格式：[{’citycode’:’110000’,’cityname’:’北京市’}]
     * @param departDate  出发时间
     * @param tel         联系人电话
     * @param wechat      微信号
     * @param budget      人均预算
     * @param crNumber    出行成人数
     * @param xhNumber    出行儿童数
     * @return
     */
    @POST("tourapi/tourCustomize/update")
    Observable<BaseDataBean<Object>> updateCustomize(@Query("token") String token,
                                                     @Query("id") int id,
                                                     @Query("departArea") String departArea,
                                                     @Query("arrivalArea") String arrivalArea,
                                                     @Query("departDate") String departDate,
                                                     @Query("tel") String tel,
                                                     @Query("wechat") String wechat,
                                                     @Query("budget") double budget,
                                                     @Query("crNumber") Integer crNumber,
                                                     @Query("xhNumber") Integer xhNumber);
    /**
     * 获取订制列表详情
     * @param token 用户token
     * @param id    私人订制id
     * @return
     */
    @POST("tourapi/tourCustomize/newSelectById")
    Observable<BaseDataBean<CustomizeForm>> getCustomizeInfo(@Query("token") String token,
                                                     @Query("id") int id);
    /**
     * 删除私人订制
     *
     * @param token 用户token
     * @param id    私人订制id
     * @return
     */
    @POST("tourapi/tourCustomize/delete")
    Observable<BaseDataBean<Object>> deleteCustomize(@Query("token") String token,
                                                     @Query("id") int id);

    /**
     * 获取私人订制列表
     *旧接口tourapi//tourCustomize/selectByList
     * @param token    用户token
     * @param pageNum
     * @param pageSize
     * @return
     */
    @POST("tourapi/tourCustomize/newSelectByList")
    Observable<BaseDataBean<List<CustomizeBean>>> getCustomizeList(@Query("token") String token,
                                                                   @Query("pageNum") int pageNum,
                                                                   @Query("pageSize") int pageSize);

    /**
     * 获取消息列表
     *
     * @param token    用户token
     * @param type     消息类型1系统通知2订单消息3活动服务4咨询服务
     * @param pageNum
     * @param pageSize
     * @return
     */
    @POST("tourapi/tourSysMsg/getList")
    Observable<BaseDataBean<List<MessageBean>>> getSysMsgList(@Query("token") String token,
                                                              @Query("type") String type,
                                                              @Query("pageNum") int pageNum,
                                                              @Query("pageSize") int pageSize);
    /**
     * 获取消息列表
     *
     * @param token    用户token
     * @param msgType     消息类型1系统通知2订单消息3活动服务4咨询服务
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("tourapi/tourSysMsg/getMsgList")
    Observable<BaseDataBean<List<HorseMiMessageBean>>> getSysMsgList(@Query("token") String token,
                                                                     @Query("msgType") String msgType,
                                                                     @Query("findMonth") String findMonth,
                                                                     @Query("pageNum") int pageNum,
                                                                     @Query("pageSize") int pageSize);
    /**
     * 获取筛选条件接口
     * @return
     */
    @GET("tourapi/tourSysMsg/getMsgScreeContent")
    Observable<BaseDataBean<List<HorseMiMessageFilterBean>>> getMsgScreeContentList();
    /**
     * 消息标记为已读
     * @return
     */
    @GET("tourapi/tourSysMsg/msgSignReadById")
    Observable<BaseDataBean<Object>> msgSignReadById(@Query("token") String token,@Query("id") int id );

    /**
     * 获取未读消息标识
     *
     * @param token 用户token
     * @return
     */
    @POST("tourapi/tourSysMsg/getNewCount")
    Observable<BaseDataBean<List<MessageTypeReadBean>>> getSysMsgNewCount(@Query("token") String token);

    /**
     * 根据消息类型批量设为已读
     *
     * @param token 用户token
     * @param types 消息类型
     * @return
     */
    @POST("tourapi/tourSysMsg/typesUpdate")
    Observable<BaseDataBean<Object>> markReadSysMsgTypesUpdate(@Query("token") String token, @Query("types") String types);

    /**
     * 获取消息列表
     *
     * @param token    用户token
     * @param pageNum
     * @param pageSize
     * @return
     */
    @POST("tourapi/tourSysMsg/getList")
    Observable<BaseDataBean<List<Object>>> getAKeySOSList(@Query("token") String token,
                                                          @Query("pageNum") int pageNum,
                                                          @Query("pageSize") int pageSize);

    /**
     * 获取获取入群信息
     *
     * @param groupId 群id
     * @return
     */
    @GET("talkback/group/room")
    Observable<BaseDataBean<GroupRoomBean>> getGroupRoomInfo(@Query("token") String token, @Query("groupId") String groupId);

    /**
     * 添加车辆
     *
     * @param groupId      群组id
     * @param licencePlate 车牌号
     * @param userId       司机用户id
     * @return
     */
    @GET("talkback/group/addDriver")
    Observable<BaseDataBean<String>> addDriver(@Query("groupId") String groupId,
                                               @Query("licencePlate") String licencePlate,
                                               @Query("userId") int userId);

    /**
     * 移除车辆
     *
     * @param groupId 群组id
     * @param userId  司机用户id 移除多个用英文逗号隔开
     * @return
     */
    @GET("talkback/group/delDriver")
    Observable<BaseDataBean<String>> delDriver(@Query("groupId") String groupId,
                                               @Query("userId") String userId);

    /**
     * 设置/更换司机
     *
     * @param driverUserId 当前车辆用户id
     * @param groupId      群组id
     * @param licencePlate 车牌号
     * @param userId       司机用户id 移除多个用英文逗号隔开
     * @param userId       更换车辆的用户id
     * @return
     */
    @GET("talkback/group/updtDriverUser")
    Observable<BaseDataBean<Object>> updtDriverUser(@Query("token") String token,
                                                    @Query("driverUserId") String driverUserId,
                                                    @Query("groupId") String groupId,
                                                    @Query("licencePlate") String licencePlate,
                                                    @Query("userId") String userId);

    /**
     * 选择司机列表
     *
     * @param groupId 群id
     * @return
     */
    @GET("talkback/group/chooseDriverList")
    Observable<BaseDataBean<List<GroupMemberBean>>> getChooseDriverList(@Query("groupId") String groupId);

    /**
     * 群成员、司机、车辆列表
     *
     * @param groupId
     * @param token
     * @param driver 是否司机(false：否 true：是)
     * @param leader 是否领队(false：否 true：是)
     * @param official 是否官方人员(false:否 true: 是)
     * @param orderId 订单号
     * @param payingUser 是否参团用户(false:否 true：是)
     * @param role
     * @param userId
     * @return
     */
    @GET("talkback/group/members")
    Observable<BaseDataBean<List<GroupMemberBean>>> getMemberList(@Query("groupId") String groupId,
                                                                  @Query("token") String token,
                                                                  @Query("driver") Boolean driver,
                                                                  @Query("leader") Boolean leader,
                                                                  @Query("official") String official,
                                                                  @Query("orderId") String orderId,
                                                                  @Query("payingUser") Boolean payingUser,
                                                                  @Query("role") Integer role,
                                                                  @Query("userId") Integer userId);

    /**
     * 设为/取消领队
     *
     * @param groupId 群id
     * @param leader  是否领队
     * @param userId  成员id
     * @return
     */
    @GET("talkback/group/updtDriverLeaderInfo")
    Observable<BaseDataBean<Object>> updtDriverLeaderInfo(@Query("groupId") String groupId,
                                                          @Query("leader") boolean leader,
                                                          @Query("userId") Integer userId);

    /**
     * 移除群成员
     *
     * @param tid     群id
     * @param userIds 被移除的用户id 多个用英文逗号,隔开
     * @return
     */
    @GET("talkback/group/removeMembers")
    Observable<BaseDataBean<Object>> removeMembers(@Query("tid") String tid,
                                                   @Query("userIds") String userIds);

    /**
     * 用户添加或者修改群成员的描叙信息
     *
     * @return
     */
    @GET("talkback/group/updtGroupMemberRemark")
    Observable<BaseDataBean<Object>> updtGroupMemberRemark(@Query("token") String token,
                                                           @Query("groupId") String groupId,
                                                           @Query("remark") String remark,
                                                           @Query("remarkUserId") String remarkUserId);

    /**
     * 用户在指定群的个人信息
     *
     * @param token    用户token
     * @param groupId  群id
     * @param memberId 群成员id
     * @return
     */
    @GET("talkback/group/user")
    Observable<BaseDataBean<GroupMemberBean>> getGroupUser(@Query("token") String token, @Query("groupId") String groupId, @Query("memberId") String memberId);

    /**
     * 用户在指定群的个人信息
     *
     * @param token    用户token
     * @param groupId  群id
     * @param memberId 群成员id
     * @return
     */
    @GET("talkback/group/user")
    Observable<BaseDataBean<GuideMapMyInfo>> getMyUser(@Query("token") String token, @Query("groupId") String groupId, @Query("memberId") String memberId);

    /**
     * 群组-目的地-集合时间 保存目的地和集合时间
     *
     * @param token                 用户token
     * @param destination           目的地
     * @param destinationCoordinate 目的地坐标
     * @param gatherTime            集合时间
     * @param groupId               群id
     * @return
     */
    @POST("talkback/group/destination")
    Observable<BaseDataBean<Object>> setGroupSettingDestination(@Query("token") String token,
                                                                @Query("destination") String destination,
                                                                @Query("destinationCoordinate") String destinationCoordinate,
                                                                @Query("gatherTime") String gatherTime,
                                                                @Query("desId") Long desId,
                                                                @Query("groupId") String groupId);

    /**
     * 群组-目的地-集合时间 获取目的地和集合时间
     *
     * @param token   用户token
     * @param groupId 群id
     * @return
     */
    @GET("talkback/group/destination")
    Observable<BaseDataBean<GetDestinationBean>> getGroupSettingDestination(@Query("token") String token, @Query("groupId") String groupId);

    /**
     * 获取行程详请
     *
     * @param token   用户token
     * @param id      行程id
     * @param refCode 推荐码
     * @param share   是否分享界面
     * @param isNewRoute   是否获取新版行程
     * @return
     */
    @GET("tourapi/tourTrip/getTripDetail")
    Observable<BaseDataBean<TourTripDetailNewBean>> getTripDetail(@Query("token") String token,
                                                                  @Query("id") int id,
                                                                  @Query("refCode") String refCode,
                                                                  @Query("groupId") String groupId,
                                                                  @Query("share") String share,
                                                                  @Query("isNewRoute") boolean isNewRoute);

    /**
     * point
     *
     * @param token  用户token
     * @param lineId 线路id
     * @param isNewRoute 是否获取新版行程
     * @return
     */
    @GET("tourapi/tourTrip/point")
    Observable<BaseDataBean<GetTripDaysInfo>> getTripPoint(@Query("token") String token, @Query("lineId") int lineId, @Query("isNewRoute") boolean isNewRoute);

    /**
     * 扫描群主二维码入群
     *
     * @param token   用户token
     * @param refCode 群主推荐码
     * @param groupId  群id
     * @return
     */
    @POST("tourapi/trip/invited/qr")
    Observable<BaseDataBean<Object>> scanQRInvited(@Query("token") String token, @Query("refCode") String refCode, @Query("groupId") String groupId);

    /**
     * 默认取不自动播放列表
     *
     * @param token token
     * @param auto  是否自动播放(0:否，1：是，默认0)
     * @return
     */
    @GET("talkback/group/autoPlayGroups")
    Observable<BaseDataBean<List<String>>> getAutoPlayGroupList(@Query("token") String token, @Query("auto") int auto);

    /**
     * 广播发起和结束发送给后台
     * 30s调一次，40s后自动清除
     *
     * @param token   token
     * @param groupId 当前群id
     * @param on      发送(true:发起广播，false：结束广播)
     * @return
     */
    @POST("talkback/group/broadcast")
    Observable<BaseDataBean<Object>> postTalkbackGroupBroadcast(@Query("token") String token,
                                                                @Query("groupId") String groupId,
                                                                @Query("on") boolean on);

    /**
     * 获取当前广播开关
     *
     * @param token   token
     * @param groupId 当前群id
     * @return
     */
    @GET("talkback/group/broadcast")
    Observable<BaseDataBean<Object>> getTalkbackGroupBroadcast(@Query("token") String token,
                                                               @Query("groupId") String groupId);

    /**
     * 是否分享位置-是否自动播放
     *
     * @param token           token
     * @param groupId         groupId
     * @param isAutoplay      isAutoplay
     * @param isShareLocation isShareLocation
     * @return
     */
    @GET("talkback/group/isShareLocationOrIsAutoplay")
    Observable<BaseDataBean<Object>> getIsShareLocationOrISAutoplay(@Query("token") String token,
                                                                    @Query("groupId") String groupId,
                                                                    @Query("isAutoplay") boolean isAutoplay,
                                                                    @Query("isShareLocation") boolean isShareLocation);
    /**
     * 自动/不自动播放群录播的群列表
     * @param token           token
     * @param auto 是否自动播放(0:否，1：是，默认0)
     * @return
     */
    @GET("talkback/group/autoPlayGroups")
    Observable<BaseDataBean<Object>> getAutoPlayGroups(@Query("token") String token,@Query("auto") int auto);

    /**
     * 修改用户群昵称
     *
     * @param token    token
     * @param groupId  groupId
     * @param nickname nickname
     * @return
     */
    @GET("talkback/group/updateGroupNickname")
    Observable<BaseDataBean<Object>> getUpdateGroupNickname(@Query("token") String token,
                                                            @Query("groupId") String groupId,
                                                            @Query("nickname") String nickname);

    /**
     * 修改群名称 群介绍
     *
     * @param token      token
     * @param groupId    groupId
     * @param intro      intro
     * @param groupTitle groupTitle
     * @return
     */
    @GET("talkback/group/updateGroupTitleAdIntro")
    Observable<BaseDataBean<Object>> getUpdateGroupTitleAdIntro(@Query("token") String token,
                                                                @Query("groupId") String groupId,
                                                                @Query("intro") String intro,
                                                                @Query("groupTitle") String groupTitle);

    /**
     * 延长解散日期
     *
     * @param token        token
     * @param groupId      groupId 群ID
     * @param dissolveDate dissolveDate 延长天数
     * @return
     */
    @GET("talkback/group/extendGroupDissolveDate")
    Observable<BaseDataBean<Object>> getExtendGroupDissolveDate(@Query("token") String token,
                                                                @Query("groupId") String groupId,
                                                                @Query("dissolveDate") int dissolveDate);

    /**
     * 退群(主动退群)
     *
     * @param groupId 群id
     * @param userId  用户ID
     * @return
     */
    @POST("talkback/group/leave")
    Observable<BaseDataBean<Object>> leaveGroup(@Query("groupId") String groupId,
                                                @Query("userId") String userId);
    /**
     *
     * 解散群(仅群主才可操作)
     * @param groupId 群id
     * @return
     */
    @GET("talkback/group/remove")
    Observable<BaseDataBean<Object>> removeGroup(@Query("groupId") String groupId);

    /**
     * 一键救援
     *
     * @param token 群id
     * @return
     */
    @GET("tourapi/tourTrip/rescue")
    Observable<BaseDataBean<Object>> rescue(@Query("token") String token);

    /**
     * 确认/取消参团
     *
     * @param groupId 群id
     * @param payingUser 是否已经参团（false 否 true 是）
     * @param userId 用户id
     * @return
     */
    @GET("talkback/group/updtGroupPayingUserState")
    Observable<BaseDataBean<Object>> updtGroupPayingUserState(@Query("token") String token,
                                            @Query("groupId") String groupId,
                                            @Query("payingUser") boolean payingUser,
                                            @Query("userId") int userId);

    /**
     * 智能组队,获取当前进行中的队伍
     * @return
     */
    @GET("talkback/group/normal")
    Observable<BaseDataBean<String>> getSmartTeamGroup(@Query("token") String token);
    /**
     * 智能组队创建群
     * @return
     */
    @POST("talkback/group/normal/create")
    Observable<BaseDataBean<Object>> createGroup(@Query("token") String token);

    /**
     * 智能组加入队伍
     * @param token
     * @param groupPwdcard 群口令
     * @return
     */
    @POST("talkback/group/joinTeam")
    Observable<BaseDataBean<Object>> joinTeam(@Query("token") String token,@Query("groupPwdcard") String groupPwdcard);
    /**
     * 智能组队-队伍详情
     * @param token
     * @param groupId 群id
     * @return
     */
    @GET("talkback/group/teamInfo")
    Observable<BaseDataBean<SmartTeamInfoBean>> getSmartTeamGroupInfo(@Query("token") String token, @Query("groupId") String groupId);
    /**
     * 智能组队-解散或退出队伍
     * @param token
     * @param groupId 群id
     * @return
     */
    @POST("talkback/group/removeTeam")
    Observable<BaseDataBean<Object>> removeTeam(@Query("token") String token, @Query("groupId") String groupId);

    /**
     * 智能组队-添加,修改,删除车辆
     * @param token
     * @param groupId 群id
     * @param addOrUpdtOrDel 添加传：1,修改传：2，删除传：3
     * @param licencePlate 添加修改必传车牌号，删除可不传
     * @return
     */
    @GET("talkback/group/teamAddOrUpdtOrDelDriver")
    Observable<BaseDataBean<Object>> smartTeamAddOrUpdtOrDelDriver(@Query("token") String token
            , @Query("groupId") String groupId
            , @Query("addOrUpdtOrDel") int addOrUpdtOrDel
            , @Query("licencePlate") String licencePlate);

    /**
     * 智能组队生成口令
     * @param token
     * @param groupId 群口令
     * @return
     */
    @POST("talkback/group/generateToken")
    Observable<BaseDataBean<Double>> generateToken(@Query("token") String token,@Query("groupId") String groupId);

    /**
     *  行程群内车辆信息
     * @param token
     * @param groupId 群口令
     * @return
     */
    @GET("talkback/group/vehicleInfo")
    Observable<BaseDataBean<List<GroupMemberBean>>> getVehicleInfo(@Query("token") String token, @Query("groupId") String groupId);

    /**
     * 当前App最新版本
     * @param appType Default value : 1 app类型 1：小马在途
     * @param clientType 客户端类型 1:android，2:ios
     * @return
     */
    @GET("user/app/version/latest")
    Observable<BaseDataBean<AppVersionBean>> getLatestVersion(@Query("appType") int appType, @Query("clientType") int clientType);

    /**
     * 指南列表接口
     * @param lineId 线路id
     * @param tripId 行程id
     * @return
     */
    @GET("/tourapi/tourSysMsg/getMsgGuideList")
    Observable<BaseDataBean<List<MsgGuideListInfo>>> getMsgGuideList(@Query("lineId") int lineId, @Query("tripId") int tripId);

    /**
     *  车辆检查指南
     * @return
     */
    @GET("/tourapi/tourGuide/car")
    Observable<BaseDataBean<List<MsgCarListInfo>>> getMsgCarInfo();

    /**
     *  根据经纬度获取当前城市的天气信息
     * @return
     */
    @GET("/tourapi/tourGuide/weather")
    Observable<BaseDataBean<WeatherInfoBean>> getWeather(@Query("token") String token,@Query("latitude") String latitude, @Query("longitude") String longitude);
    /**
     * 消息未读数量
     *
     * @param token ：token
     * @return 未读数量
     */
    @GET("tourapi/tourSysMsg/getMsgUnreadCount")
    Observable<BaseDataBean<Integer>> getMsgUnreadCount(@Query("token") String token);

    /**
     * 获取车辆列表
     * @param token ：token
     * @param pageNum 当前第几页(默认1，从1开始)
     * @param pageSize 每页大小(默认20)
     * @return
     */
    @GET("tourapi/sysUserPlateNumber/list")
    Observable<BaseDataBean<List<CommonVehicleBean>>> getsysUserPlateNumberList(@Query("token") String token, @Query("pageNum")int pageNum, @Query("pageSize")int pageSize);

    /**
     * 添加或修改车辆
     * @param token ：token
     * @param plateNumber 车牌号
     * @param id  数据id
     * @return
     */
    @POST("tourapi/sysUserPlateNumber/save")
    Observable<BaseDataBean<String>> getsysUserPlateNumberSave(@Query("token") String token,@Query("plateNumber")String plateNumber,@Query("id")Integer id,@Query("isDefault")int isDefault);

    /**
     * 删除常用车辆
     * @param token ：token
     * @param id  数据id
     * @return
     */
    @GET("tourapi/sysUserPlateNumber/delete")
    Observable<BaseDataBean<String>> getsysUserPlateNumberDelete(@Query("token") String token,@Query("id")Integer id);

    /**
     *  获取默认的车辆信息
     * @param token ：token
     * @return
     */
    @GET("tourapi/sysUserPlateNumber/selectDefault")
    Observable<BaseDataBean<CommonVehicleBean>> getsysUserPlateNumberSelectDefault(@Query("token") String token);

    /**
     *  智慧导览-进行中队伍
     * @param token 用户token
     * @param groupId 群id (传就返回该群的群信息，不传查询所有进行中的群信息)
     * @return
     */
    @GET("user/sysUserGroupSetup/getUserGroupSetups")
    Observable<BaseDataBean<MyTalkGroupsBean>> getUserGroupSetups(@Query("token") String token, @Query("groupId") String groupId);

    /**
     *  智慧导览-设置当前队伍，智慧导览-设置是否显示成员位置开关
     * @param token 用户token
     * @param groupId 群id（注：与是否显示成员位置选传,两个必须传一个）
     * @param isPosition 是否显示成员位置：1 否 2 是 （注：与群id选传,两个必须传一个）
     * @return
     */
    @POST("user/sysUserGroupSetup/setUserGroupOrPosition")
    Observable<BaseDataBean<String>> setUserGroupOrPosition(@Query("token") String token,@Query("groupId") String groupId,@Query("isPosition") Integer isPosition);

    /**
     * 群组-目的地-集合时间 用户签到
     *
     * @param token   用户token
     * @param desId   查询目的地-集合时间接口返回的id
     * @return
     */
    @POST("talkback/group/destination/signin")
    Observable<BaseDataBean<Object>> setSignIn(@Query("token") String token, @Query("desId") String desId);

    /**
     * 群组-目的地-集合时间 检测是否签到
     *
     * @param token   用户token
     * @param desId   查询目的地-集合时间接口返回的id
     * @return
     */
    @GET("talkback/group/destination/signinCheck")
    Observable<BaseDataBean<TripSignInDetailBean>> getSignInCheck(@Query("token") String token, @Query("desId") String desId);

    /**
     * 群组-目的地-集合时间 用户签到记录信息
     *
     * @param token   用户token
     * @param desId   查询目的地-集合时间接口返回的id
     * @return
     */
    @GET("talkback/group/destination/signinInfos")
    Observable<BaseDataBean<TripSignInListBean>> getSigninInfos(@Query("token") String token, @Query("desId") String desId);

    /**
     * 用户消息全部设为已读
     *
     * @param token   用户token
     * @return
     */
    @GET("tourapi/tourSysMsg/allMsgAlreadyRead")
    Observable<BaseDataBean<Object>> allMsgAlreadyRead(@Query("token") String token);

    /**
     * 获取通过城市名字首字母排序过后的数据
     * @param areaType 城市类型，多个请使用英文逗号拼接：1省 2市 3区
     * @return
     */
    @GET("tourapi/sys/areas/getSortArea")
    Observable<BaseDataBean<List<CityBean>>> getSortArea(@Query("areaType") String areaType);

    /**
     * 景区离线资源包下载列表
     * @param scenicIds 景区编号列表
     * @return
     */
    @GET("scenic/officeline/packageDowns")
    Observable<BaseDataBean<List<OfflineFileManagerBean>>> getOfficeLinePackageDowns(@Query("scenicIds") String scenicIds);

    /**
     * 线路行程详情（新版）
     * @param lineId 线路id
     * @return
     */
    @GET("tourapi/line/route/detail")
    Observable<BaseDataBean<List<TourTripDetailNewBean.LineRouteListVOBean>>> getRoutedetail(@Query("lineId") long lineId);
}

