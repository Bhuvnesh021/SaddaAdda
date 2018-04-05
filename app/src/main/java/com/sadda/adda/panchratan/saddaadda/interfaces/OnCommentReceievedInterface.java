package com.sadda.adda.panchratan.saddaadda.interfaces;

import com.sadda.adda.panchratan.saddaadda.objects.Comment;
import com.sadda.adda.panchratan.saddaadda.objects.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22-07-2017.
 */
public interface OnCommentReceievedInterface {
    void onAllCommentsReceieved(List<Comment> list);
    void onAllItemsReceived(List<Item> list);
}
