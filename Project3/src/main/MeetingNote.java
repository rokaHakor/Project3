package main;

public class MeetingNote {

    private int meetingNoteID, decisionID;
    private String note;

    public MeetingNote(int meetingNoteID, int decisionID, String note) {
        this.meetingNoteID = meetingNoteID;
        this.decisionID = decisionID;
        this.note = note;
    }

    public int getMeetingNoteID() { return meetingNoteID; }
    public void setMeetingNoteID(int meetingNoteID) { this.meetingNoteID = meetingNoteID; }
    public int getDecisionID() { return decisionID; }
    public void setDecisionID(int decisionID) { this.decisionID = decisionID; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
