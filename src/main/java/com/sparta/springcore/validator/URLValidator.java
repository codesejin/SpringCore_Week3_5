package com.sparta.springcore.validator;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLValidator {
    /*
    url체크하는게 ProductValidator클래스에서만 사용되지 않고,
    다른 여러군데서 쓰임이 필요할때사용하기 편하게 하기 위해 리팩토링
     */
    public static boolean isValidUrl(String url)
    {
        try {
            //URL은 url형태로 만들어주는 java.net.class
            new URL(url).toURI();
            return true;
        }
        //원하던 url패스가 아닐경우 에러
        catch (URISyntaxException | MalformedURLException exception) {
            return false;
        }
    }
}
