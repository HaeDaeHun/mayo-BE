package ukathon.mayo.domain.reference;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    BEAUTY("미용·뷰티"),
    EDUCATION("학원·교육"),
    FOOD("요식업"),
    FASHION("패션·잡화"),
    NUTRITION("식음료·제약"),
    LIVING("생활용품"),
    TRAVEL("여행·숙박"),
    HANDCRAFT("수공업"),
    PET("반려동물"),
    EVENTS("이벤트"),
    FITNESS("운동"),
    OTHERS("기타");

    private final String description;
}
