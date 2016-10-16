package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haseeb on 15/10/16.
 */
public class MessageContent {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("participants")
    @Expose
    private List<Participant> participants = new ArrayList<Participant>();
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("isRead")
    @Expose
    private Boolean isRead;
    @SerializedName("isStarred")
    @Expose
    private Boolean isStarred;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("ts")
    @Expose
    private Integer ts;

    /**
     *
     * @return
     * The subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @param subject
     * The subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     *
     * @return
     * The participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     *
     * @param participants
     * The participants
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     *
     * @return
     * The preview
     */
    public String getPreview() {
        return preview;
    }

    /**
     *
     * @param preview
     * The preview
     */
    public void setPreview(String preview) {
        this.preview = preview;
    }

    /**
     *
     * @return
     * The isRead
     */
    public Boolean getIsRead() {
        return isRead;
    }

    /**
     *
     * @param isRead
     * The isRead
     */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    /**
     *
     * @return
     * The isStarred
     */
    public Boolean getIsStarred() {
        return isStarred;
    }

    /**
     *
     * @param isStarred
     * The isStarred
     */
    public void setIsStarred(Boolean isStarred) {
        this.isStarred = isStarred;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The body
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body
     * The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     *
     * @return
     * The ts
     */
    public Integer getTs() {
        return ts;
    }

    /**
     *
     * @param ts
     * The ts
     */
    public void setTs(Integer ts) {
        this.ts = ts;
    }

}

