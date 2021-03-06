package com.whatmedia.ttia.page.main.home.departure;


import com.whatmedia.ttia.response.data.FlightSearchData;
import com.whatmedia.ttia.response.data.FlightsInfoData;

import java.util.List;

public interface DepartureFlightsContract {

    interface View {
        void getDepartureFlightSucceed(List<FlightsInfoData> list);

        void getDepartureFlightFailed(String message, boolean timeout);

        void saveMyFlightSucceed(String message);

        void saveMyFlightFailed(String message, boolean timeout);
    }

    interface Presenter {
        void getDepartureFlightAPI(FlightSearchData searchData);

        void saveMyFlightsAPI(FlightsInfoData data);
    }
}
