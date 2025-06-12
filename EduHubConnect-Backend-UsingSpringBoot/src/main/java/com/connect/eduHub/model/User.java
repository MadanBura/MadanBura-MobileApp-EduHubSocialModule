package com.connect.eduHub.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String password;
    private String bio;
    private String profileImage;
    private String profileBackgroundBanner="";
    private String collegeName = "";
    private String about="";
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "sender")
    private List<FriendRequest> sentRequests;

    @OneToMany(mappedBy = "receiver")
    private List<FriendRequest> receivedRequests;

    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "receiver")
    private List<Message> messagesReceived;
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Education> educationList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Skill> skills;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserFcmToken fcmToken;
    
    
    @Lob
    private byte[] publicKey;

    @Lob
    private byte[] privateKey;
    
}
