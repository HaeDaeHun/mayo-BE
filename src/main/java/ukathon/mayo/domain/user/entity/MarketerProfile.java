package ukathon.mayo.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ukathon.mayo.domain.matching.Region;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MarketerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String portfolioUrl;

    private String education;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region activityRegion;

    @Column(nullable = false)
    private int marketingCount;
}

