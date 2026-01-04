package miniproject;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

public enum PageType {

    CREATE_ACCOUNT(
            "https://ecommerce.tealiumdemo.com/customer/account/create/",
            "Create New Customer Account"
    ),
    HOME(
            "https://ecommerce.tealiumdemo.com/",
            "Tealium Ecommerce Demo"
    ),
    ;

    PageType(String url, @Nullable String title) {
        this.url = Validate.notBlank(url, "url is required");
        this.title = title;
    }

    private final String url;
    private final String title;

    public String getUrl() {
        return this.url;
    }

    public String getTitle() {
        return this.title;
    }

}
