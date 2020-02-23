package app.flora.driver.webservices.models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String created_on_utc;
    private BillingAddress billing_address;
    private ShippingAddress shipping_address;
    private String order_status;
    public String shipping_status;
    private String id;

    private  int deliverer_id;

    public int getDeliverer_id() {
        return deliverer_id;
    }

    public void setDeliverer_id(int deliverer_id) {
        this.deliverer_id = deliverer_id;
    }

    public String getCreated_on_utc() {
        return created_on_utc;
    }

    public void setCreated_on_utc(String created_on_utc) {
        this.created_on_utc =  created_on_utc;
    }


    public BillingAddress getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(BillingAddress billing_address) {
        this.billing_address = billing_address;
    }

    public ShippingAddress getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(ShippingAddress shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getOrder_status() {
        return order_status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static class BillingAddress {
        /**
         * first_name : Mohamed
         * last_name : Abdulrahim
         * email : admin@hardtask.com
         * company : null
         * country_id : 143
         * country : Kuwait
         * state_province_id : 76
         * city : null
         * block : 1
         * address1 : 12
         * address2 : Extra
         * zip_postal_code : null
         * phone_number : 98765432
         * fax_number : null
         * customer_attributes :
         * created_on_utc : 2019-05-20T12:36:00.4502734
         * province : Abdullah Al-Salem
         * id : 42
         */

        private String first_name;
        private String last_name;
        private String email;
        private Object company;
        private int country_id;
        private String country;
        private int state_province_id;
        private Object city;
        private String block;
        private String address1;
        private String address2;
        private Object zip_postal_code;
        private String phone_number;
        private Object fax_number;
        private String customer_attributes;
        private String created_on_utc;
        private String province;
        private int id;



        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getCompany() {
            return company;
        }

        public void setCompany(Object company) {
            this.company = company;
        }

        public int getCountry_id() {
            return country_id;
        }

        public void setCountry_id(int country_id) {
            this.country_id = country_id;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getState_province_id() {
            return state_province_id;
        }

        public void setState_province_id(int state_province_id) {
            this.state_province_id = state_province_id;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public Object getZip_postal_code() {
            return zip_postal_code;
        }

        public void setZip_postal_code(Object zip_postal_code) {
            this.zip_postal_code = zip_postal_code;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public Object getFax_number() {
            return fax_number;
        }

        public void setFax_number(Object fax_number) {
            this.fax_number = fax_number;
        }

        public String getCustomer_attributes() {
            return customer_attributes;
        }

        public void setCustomer_attributes(String customer_attributes) {
            this.customer_attributes = customer_attributes;
        }

        public String getCreated_on_utc() {
            return created_on_utc;
        }

        public void setCreated_on_utc(String created_on_utc) {
            this.created_on_utc = created_on_utc;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class ShippingAddress implements Serializable {
        /**
         * first_name : Mohamed
         * last_name : Abdulrahim
         * email : admin@hardtask.com
         * company : null
         * country_id : 143
         * country : Kuwait
         * state_province_id : 76
         * city : null
         * block : 1
         * address1 : 12
         * address2 : Extra
         * zip_postal_code : null
         * phone_number : 98765432
         * fax_number : null
         * customer_attributes :
         * created_on_utc : 2019-05-20T12:36:00.4502734
         * province : Abdullah Al-Salem
         * id : 43
         */

        private String first_name;
        private String last_name;
        private String email;
        private Object company;
        private int country_id;
        private String country;
        private int state_province_id;
        private Object city;
        private String block;
        private String address1;
        private String address2;
        private Object zip_postal_code;
        private String phone_number;
        private Object fax_number;
        private String customer_attributes;
        private String created_on_utc;
        private String province;
        private int id;

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getCompany() {
            return company;
        }

        public void setCompany(Object company) {
            this.company = company;
        }

        public int getCountry_id() {
            return country_id;
        }

        public void setCountry_id(int country_id) {
            this.country_id = country_id;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getState_province_id() {
            return state_province_id;
        }

        public void setState_province_id(int state_province_id) {
            this.state_province_id = state_province_id;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public Object getZip_postal_code() {
            return zip_postal_code;
        }

        public void setZip_postal_code(Object zip_postal_code) {
            this.zip_postal_code = zip_postal_code;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public Object getFax_number() {
            return fax_number;
        }

        public void setFax_number(Object fax_number) {
            this.fax_number = fax_number;
        }

        public String getCustomer_attributes() {
            return customer_attributes;
        }

        public void setCustomer_attributes(String customer_attributes) {
            this.customer_attributes = customer_attributes;
        }

        public String getCreated_on_utc() {
            return created_on_utc;
        }

        public void setCreated_on_utc(String created_on_utc) {
            this.created_on_utc = created_on_utc;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

