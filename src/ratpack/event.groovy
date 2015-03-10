class event {
    String title
    String date
    String state
    String city

    event(String title, String date, String state, String city) {
        this.title = title
        this.date = date
        this.state = state
        this.city = city
    }

    String getTitle() {
        return title
    }

    String getDate() {
        return date
    }

    String getState() {
        return state
    }

    String getCity() {
        return city
    }

    void setTitle(String title) {
        this.title = title
    }

    void setDate(String date) {
        this.date = date
    }

    void setState(String state) {
        this.state = state
    }

    void setCity(String city) {
        this.city = city
    }
}