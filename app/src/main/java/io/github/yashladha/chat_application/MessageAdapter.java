package io.github.yashladha.chat_application;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Message> messages;
    private final int SENDER = 0, RECEIVER = 1;
    private String curUid;

    public MessageAdapter(String curUid, ArrayList<Message> messages) {
        this.curUid = curUid;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case SENDER:
                View v1 = layoutInflater.inflate(R.layout.chat_right, parent, false);
                viewHolder = new SenderViewHolder(v1);
                return viewHolder;
            case RECEIVER:
                View v2 = layoutInflater.inflate(R.layout.chat_left, parent, false);
                viewHolder = new ReceiverViewHolder(v2);
                return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        switch (holder.getItemViewType()) {
            case SENDER:
                SenderViewHolder viewHolder = (SenderViewHolder) holder;
                viewHolder.textView.setText(message.getText());
                break;
            case RECEIVER:
                ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
                receiverViewHolder.textView.setText(message.getText());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getID().equals(curUid))
            return SENDER;
        else
            return RECEIVER;
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public SenderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvMessageInput);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ReceiverViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvMessageInput);
        }
    }
}
