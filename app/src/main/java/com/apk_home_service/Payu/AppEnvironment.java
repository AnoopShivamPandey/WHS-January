package com.apk_home_service.Payu;


/**
 * Created by Rahul Hooda on 14/7/17.
 */
public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {

//            return "QylhKRVd";
            return "0mjWc4oH";
        }

        @Override
        public String merchant_ID() {
            return "6593859";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {

//            return "seVTUgzrgE";
            return "kztO1oahjB";
        }

        @Override
        public boolean debug() {
            return false;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
//            return "QylhKRVd";
            return "0mjWc4oH";
        }
        @Override
        public String merchant_ID() {
//            return "5960507";
            return "6593859";
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

//        @Override
//        public String salt() {
//            return "seVTUgzrgE";
//        }
        public String salt() {
            return "kztO1oahjB";
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
