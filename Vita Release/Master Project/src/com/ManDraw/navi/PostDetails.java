package com.ManDraw.navi;

public class PostDetails {
    String image_path;
    String audio_path;
    String user_id;
    String user_name;

    PostDetails() {
        image_path = null;
        audio_path = null;
        user_id = null;
        user_name = null;
    }

    public String getImagePath() {
        return image_path;
    }

    public void setImagePath(String imag_path) {
        this.image_path = imag_path;
    }

    public String getAudioPath() {
        return audio_path;
    }

    public void setAudioPath(String aud_path) {
        this.audio_path = aud_path;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String use_id) {
        this.user_id = use_id;
    }

    public String getUsername() {
        return user_name;
    }

    public void setUsername(String usern_id) {
        this.user_name = usern_id;
    }
}
