package com.company.collegenoticeboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beans.Notice;

import java.util.ArrayList;


public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationHolder> {

    private  ArrayList<Notice> notices;

    public NotificationsListAdapter(ArrayList<Notice> notices) {
        this.notices = notices;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        Notice notice = notices.get(position);
        holder.setNotice(notice);
    }

    @Override
    public int getItemCount() {
        return notices == null ? 0 : notices.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder {

        private TextView noticeTitle;
        private TextView noticeMessage;
        private ImageView imageView;

        NotificationHolder(@NonNull View itemView) {
            super(itemView);
            noticeTitle = itemView.findViewById(R.id.notificationTitle);
            noticeMessage = itemView.findViewById(R.id.notificationMessage);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void setNotice(Notice notice) {
            noticeTitle.setText(notice.getTitle());
            noticeMessage.setText(notice.getMessage());
            imageView.setImageResource(R.drawable.ic_notifications_icon);
        }
    }
}
