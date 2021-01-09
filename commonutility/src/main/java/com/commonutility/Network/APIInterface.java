package com.commonutility.Network;

import com.commonutility.ModelClass.AppointmentListData;
import com.commonutility.ModelClass.ArtistListData;
import com.commonutility.ModelClass.BookingDetailData;
import com.commonutility.ModelClass.CancelBookingData;
import com.commonutility.ModelClass.ChangePassData;
import com.commonutility.ModelClass.CreateArtistData;
import com.commonutility.ModelClass.DeleteArtistData;
import com.commonutility.ModelClass.DeleteServiceData;
import com.commonutility.ModelClass.FeedbackData;
import com.commonutility.ModelClass.ForgotPassData;
import com.commonutility.ModelClass.GetAllServiceData;
import com.commonutility.ModelClass.GetSalesData;
import com.commonutility.ModelClass.GetServiceData;
import com.commonutility.ModelClass.GetTicketData;
import com.commonutility.ModelClass.GetTransactionData;
import com.commonutility.ModelClass.LoadMessageData;
import com.commonutility.ModelClass.LoginData;
import com.commonutility.ModelClass.LoginStatusData;
import com.commonutility.ModelClass.NotifyListData;
import com.commonutility.ModelClass.PageData;
import com.commonutility.ModelClass.RatingData;
import com.commonutility.ModelClass.RemoveImageData;
import com.commonutility.ModelClass.Result;
import com.commonutility.ModelClass.SearchData;
import com.commonutility.ModelClass.ServiceListData;
import com.commonutility.ModelClass.UpdateScheduleData;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.mime.TypedFile;

/**
 * Created by Rajnish on 6/27/2017.
 */

public interface APIInterface {

    @POST("/api/login")
    void userLogin(@Body LoginData loginData, Callback<Result> callback);

    @POST("/api/forgotpassword")
    void forgotpassword(@Body ForgotPassData loginData, Callback<Result> callback);

    @POST("/tallah/BookingList")
    void getBookingList(@Body AppointmentListData loginData, Callback<Result> callback);

    @POST("/salon/GetArtistList")
    void GetArtistList(@Body ArtistListData loginData, Callback<Result> callback);

    @POST("/tallah/BookingDetailsForSalon")
    void GetBookingDetailsForSalon(@Body BookingDetailData loginData, Callback<Result> callback);

    @POST("/Salon/GetServiceListBySalonID")
    void GetServiceListBySalonID(@Body ServiceListData loginData, Callback<Result> callback);

    @POST("/Salon/DeleteServiceBySalonID")
    void DeleteServiceBySalonID(@Body DeleteServiceData loginData, Callback<Result> callback);

    @GET("/api/customer/salon/servicecategory")
    void getServiceCategory(Callback<Result> callback);

    @POST("/api/customer/salon/subservice")
    void getServices(@Body GetServiceData getServiceData, Callback<Result> callback);

    @POST("/Salon/InsertServiceBySalonID")
    void InsertServiceBySalonID(@Body DeleteServiceData loginData, Callback<Result> callback);

    @POST("/api/salonowner/services/changeserviceprice")
    void ChangeServiceBySalonID(@Body DeleteServiceData loginData, Callback<Result> callback);



    @POST("/salon/AddArtistToSalon")
    void AddArtistToSalon(@Body DeleteArtistData loginData, Callback<Result> callback);

    @POST("/salon/GetFreeArtistList")
    void GetFreeArtistList(@Body ArtistListData loginData, Callback<Result> callback);

    @Multipart
    @POST("/api/ArtistProfile/InsertArtist")
    void InsertArtist(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);

    @Multipart
    @POST("/salon/CreateArtist")
    void CreateArtist(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);


    @Multipart
    @POST("/salon/UpdateArtist")
    void UpdateArtist(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);


    @POST("/salon/GetArtistDetailByArtistID")
    void GetArtistDetailByArtistID(@Body DeleteArtistData loginData, Callback<Result> callback);

    @POST("/tallah/CancelBooking")
    void CancelBooking(@Body CancelBookingData loginData, Callback<Result> callback);

    @POST("/tallah/ApproveBooking")
    void ApproveBooking(@Body BookingDetailData loginData, Callback<Result> callback);

    @POST("/api/customer/profile/changepassword")
    void changepassword(@Body ChangePassData getServiceData, Callback<Result> callback);

    @POST("/Salon/GetSalonByID")
    void GetSalonByID(@Body ServiceListData loginData, Callback<Result> callback);

    @POST("/api/pushnotification/notificationlist")
    void GetNotificationListByUser(@Body NotifyListData loginData, Callback<Result> callback);

    @POST("/Salon/GetRatingListBySalonID")
    void GetRatingListBySalonID(@Body ServiceListData loginData, Callback<Result> callback);

    @POST("/tallah/GetPaymentList")
    void getPaymentList(@Body GetTransactionData getServiceData, Callback<Result> callback);

    @Multipart
    @POST("/api/salonowner/profile/update")
    void CreateSalon(@PartMap Map<String, String> data, Callback<Result> callback);
//    void CreateSalon(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);


    @Multipart
    @POST("/api/salonprofile")
    void UpdateSalonPic(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);

    @POST("/salon/UpdateSalonOperationHours")
    void UpdateSalon(@Body UpdateScheduleData getServiceData, Callback<Result> callback);

    @POST("/Salon/GetSales")
    void GetSales(@Body GetSalesData getServiceData, Callback<Result> callback);

    @POST("/api/message/messagehistry")
    void messagehistry(@Body LoadMessageData loginData, Callback<Result> callback);

    @POST("/Salon/GetReportforSalon")
    void GetReport(@Body GetSalesData loginData, Callback<Result> callback);

    @POST("/tallah/GetUserDetails")
    void GetUserDetails(@Body LoginStatusData loginData, Callback<Result> callback);

    @POST("/api/customer/ratings/addrating")
    void addrating(@Body RatingData ratingData, Callback<Result> callback);

    @POST("/api/message")
    void loadAllMessage(@Body LoadMessageData loginData, Callback<Result> callback);

    @Multipart
    @POST("/api/message/add")
    void messageAdd(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);

    @POST("/api/salonowner/service/AddServicetoSalon")
    void getAllServiceData(@Body GetAllServiceData loginData, Callback<Result> callback);

    @POST("/api/customer/tickets/detail")
    void ticketDetail(@Body GetTicketData getServiceData, Callback<Result> callback);

    @Multipart
    @POST("/api/customer/tickets/addtickets")
    void ticketGenerate(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);

    @GET("/api/customer/tickets")
    void getTicketCategoryList(Callback<Result> callback);

    @POST("/api/customer/tickets/ticketslist")
    void searchTicket(@Body SearchData loginData, Callback<Result> callback);

    @POST("/api/pages")
    void getPages(@Body PageData loginData, Callback<Result> callback);

    @POST("/api/pages/contacts")
    void contactUs(@Body FeedbackData loginData, Callback<Result> callback);

    @POST("/api/customer/booking/bookedslots")
    void getBookedslots(@Body AppointmentListData loginData, Callback<Result> callback);


    @POST("/api/salonowner/gallery")
    void getGallery(@Body AppointmentListData loginData, Callback<Result> callback);

    @POST("/api/salonowner/gallery/remove")
    void removeImage(@Body RemoveImageData loginData, Callback<Result> callback);


    @Multipart
    @POST("/api/salonowner/gallery/uploads")
    void uploadImage(@PartMap Map<String, TypedFile> Files, @PartMap Map<String, String> data, Callback<Result> callback);


}
