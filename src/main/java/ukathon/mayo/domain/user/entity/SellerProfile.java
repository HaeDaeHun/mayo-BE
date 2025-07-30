package ukathon.mayo.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ukathon.mayo.domain.matching.Region;
import ukathon.mayo.domain.reference.entity.Category;
import ukathon.mayo.domain.reference.entity.Channel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SellerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String storeName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region storeRegion;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel preferredChannel;

    @Column(nullable = false)
    private boolean isVerified;

    @Builder
    public SellerProfile(Long id, User user, String storeName, Category category, Region storeRegion, String address, Channel preferredChannel, boolean isVerified) {
        this.id = id;
        this.user = user;
        this.storeName = storeName;
        this.category = category;
        this.storeRegion = storeRegion;
        this.address = address;
        this.preferredChannel = preferredChannel;
        this.isVerified = isVerified;
    }
}

