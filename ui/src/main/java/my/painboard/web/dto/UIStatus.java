package my.painboard.web.dto;

public enum UIStatus {
    IMAGE("image"),
    HOLIDAY("holiday"),
    VACATION("vacation"),
    IMAGE_AND_FILL("image_and_fill"),
    TOFILL("tofill"),
    EMPTY("empty");

    private String status;

    UIStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
