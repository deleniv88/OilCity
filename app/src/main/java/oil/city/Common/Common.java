package oil.city.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import oil.city.Model.User;

public class Common {

    public static User currentUser;
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static final String INTENT_EVENT_ID = "EventId";
    public static final String INTENT_RESTORAUNT_ID = "RestorauntId";
    public static final String INTENT_SPORT_ID = "SportId";
    public static final String INTENT_HOTEL_ID = "HotelId";
    public static final String INTENT_SKI_ID = "SkiId";

    public static final String DELETE = "Видалити";
    public static final String UPDATE = "Оновити";

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i=0; i<info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return  true;
                }
            }
        }
        return false;
    }
}
