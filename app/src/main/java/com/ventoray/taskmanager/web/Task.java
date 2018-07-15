package com.ventoray.taskmanager.web;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Nick on 7/8/2018.
 */

public class Task implements Parcelable {

    public static final int STATUS_NOT_STARTED = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_DONE = 2;
    public static final int STATUS_ERROR = 101;

    private String taskName;
    private int status;
    private String mysqlTimestamp;
    private int uniqueId;

    public Task() {

    }

    public Task(int uniqueId, String taskName, int status, String mysqlTimestamp) {
        this.uniqueId = uniqueId;
        this.taskName = taskName;
        this.status = status;
        this.mysqlTimestamp = mysqlTimestamp;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMysqlTimestamp() {
        return mysqlTimestamp;
    }

    public void setMysqlTimestamp(String mysqlTimestamp) {
        this.mysqlTimestamp = mysqlTimestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.taskName);
        dest.writeInt(this.status);
        dest.writeString(this.mysqlTimestamp);
        dest.writeInt(this.uniqueId);
    }

    protected Task(Parcel in) {
        this.taskName = in.readString();
        this.status = in.readInt();
        this.mysqlTimestamp = in.readString();
        this.uniqueId = in.readInt();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
