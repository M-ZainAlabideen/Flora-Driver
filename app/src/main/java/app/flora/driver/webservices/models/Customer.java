package app.flora.driver.webservices.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Customer {
    @SerializedName("name")
    private String name;

    @SerializedName("min_reward_points_balance")
    private Integer minRewardPointsBalance;

    @SerializedName("reward_points_balance")
    private Integer rewardPointsBalance;

    @SerializedName("reward_points_amount")
    private String rewardPointsAmount;

    @SerializedName("min_reward_points_amount")
    private String minRewardPointsAmount;

    @SerializedName("password")
    private String password;

    @SerializedName("gender")
    private String gender;

    @SerializedName("date_of_birth")
    private String date_of_birth;

    @SerializedName("subscribed_to_newsletter")
    private String subscribed_to_newsletter;

    @SerializedName("shopping_cart_items")
    private List<Object> shoppingCartItems = null;

    @SerializedName("billing_address")
    private Address billingAddress;

    @SerializedName("shipping_address")
    private ShippingAddress shippingAddress;

    @SerializedName("addresses")
    private List<Address> addresses = null;

    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("picture_url")
    private String picture_url;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("is_tax_exempt")
    private Boolean isTaxExempt;

    @SerializedName("has_shopping_cart_items")
    private Boolean hasShoppingCartItems;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("deleted")
    private Boolean deleted;

    @SerializedName("is_system_account")
    private Boolean isSystemAccount;

    @SerializedName("last_ip_address")
    private String lastIpAddress;

    @SerializedName("created_on_utc")
    private String createdOnUtc;

    @SerializedName("last_login_date_utc")
    private String lastLoginDateUtc;

    @SerializedName("last_activity_date_utc")
    private String lastActivityDateUtc;

    @SerializedName("role_ids")
    private List<Integer> roleIds = null;

    @SerializedName("customer_guid")
    private String customerGuid;

    @SerializedName("vendor_id")
    private String vendor_id;

    public String getPicture_url() {
        return picture_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinRewardPointsBalance() {
        return minRewardPointsBalance;
    }

    public void setMinRewardPointsBalance(Integer minRewardPointsBalance) {
        this.minRewardPointsBalance = minRewardPointsBalance;
    }

    public Integer getRewardPointsBalance() {
        return rewardPointsBalance;
    }

    public void setRewardPointsBalance(Integer rewardPointsBalance) {
        this.rewardPointsBalance = rewardPointsBalance;
    }

    public String getRewardPointsAmount() {
        return rewardPointsAmount;
    }

    public void setRewardPointsAmount(String rewardPointsAmount) {
        this.rewardPointsAmount = rewardPointsAmount;
    }

    public String getMinRewardPointsAmount() {
        return minRewardPointsAmount;
    }

    public void setMinRewardPointsAmount(String minRewardPointsAmount) {
        this.minRewardPointsAmount = minRewardPointsAmount;
    }

    public String getCustomerGuid() {
        return customerGuid;
    }

    public void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getSubscribed_to_newsletter() {
        return subscribed_to_newsletter;
    }

    public void setSubscribed_to_newsletter(String subscribed_to_newsletter) {
        this.subscribed_to_newsletter = subscribed_to_newsletter;
    }

    public List<Object> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(List<Object> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getIsTaxExempt() {
        return isTaxExempt;
    }

    public void setIsTaxExempt(Boolean isTaxExempt) {
        this.isTaxExempt = isTaxExempt;
    }

    public Boolean getHasShoppingCartItems() {
        return hasShoppingCartItems;
    }

    public void setHasShoppingCartItems(Boolean hasShoppingCartItems) {
        this.hasShoppingCartItems = hasShoppingCartItems;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getIsSystemAccount() {
        return isSystemAccount;
    }

    public void setIsSystemAccount(Boolean isSystemAccount) {
        this.isSystemAccount = isSystemAccount;
    }

    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }

    public String getCreatedOnUtc() {
        return createdOnUtc;
    }

    public void setCreatedOnUtc(String createdOnUtc) {
        this.createdOnUtc = createdOnUtc;
    }

    public String getLastLoginDateUtc() {
        return lastLoginDateUtc;
    }

    public void setLastLoginDateUtc(String lastLoginDateUtc) {
        this.lastLoginDateUtc = lastLoginDateUtc;
    }

    public String getLastActivityDateUtc() {
        return lastActivityDateUtc;
    }

    public void setLastActivityDateUtc(String lastActivityDateUtc) {
        this.lastActivityDateUtc = lastActivityDateUtc;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

}
