package com.example.cuchats.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuchats.databinding.ItemContainerRecentConversationBinding;
import com.example.cuchats.listeners.ConversionListener;
import com.example.cuchats.models.ChatMessage;
import com.example.cuchats.models.User;

import java.util.List;

public class RecentConversationAdapter extends  RecyclerView.Adapter<RecentConversationAdapter.ConversionViewHolder>{
    private  final List<ChatMessage> chatMessageList;
    private final ConversionListener conversionListener;
    public RecentConversationAdapter(List<ChatMessage> chatMessages , ConversionListener conversionListener){
        this.chatMessageList = chatMessages;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversationBinding.inflate(LayoutInflater.from(parent.getContext()),parent , false)
        );
        }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
    holder.setData(chatMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversationBinding binding;
        ConversionViewHolder(ItemContainerRecentConversationBinding itemContainerRecentConversationBinding){
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }
        void setData(ChatMessage chatMessage){
            binding.imageProfile.setImageBitmap(getCoversionImage(chatMessage.conversionImage));
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.getRoot().setOnClickListener(v->{
                User user = new User();
                user.id = chatMessage.conversionId;
                user.name = chatMessage.conversionName;
                user.image = chatMessage.conversionImage;
                conversionListener.onConversionClicked(user);
            });
        }
    }

    private Bitmap getCoversionImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
