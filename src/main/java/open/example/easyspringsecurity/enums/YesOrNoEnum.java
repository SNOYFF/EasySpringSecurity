package open.example.easyspringsecurity.enums;

import lombok.Getter;

@Getter
public enum YesOrNoEnum {

    /**
     * 0否1是
     */
    NO("0","否"),
    YES("1","是");
     YesOrNoEnum(String code,String desc){
        this.code =code;
        this.desc = desc;
    }
    private String code;
    private String desc;
}
