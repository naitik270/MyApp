package com.demo.nspl.restaurantlite.RxContacts;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.R;

import java.util.ArrayList;
import java.util.List;

class MultiContactPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Contact> contactItemList = new ArrayList<>();
    private List<Contact> contactItemListOriginal = new ArrayList<>();
    private ContactSelectListener listener;
    private String currentFilterQuery;

    private boolean isSelected[];


    interface ContactSelectListener {
        void onContactSelected(Contact contact, int totalSelectedContacts);
    }

    MultiContactPickerAdapter(List<Contact> contactItemList, ContactSelectListener listener) {
        this.contactItemList = contactItemList;
        this.contactItemListOriginal = contactItemList;
        this.listener = listener;
        isSelected = new boolean[contactItemList.size()];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_contact_pick_item, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        if (holder instanceof ContactViewHolder) {
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            final Contact contactItem = getItem(i);
            contactViewHolder.tvContactName.setText(contactItem.getDisplayName());
//            contactViewHolder.vRoundLetterView.setTitleText(String.valueOf(contactItem.getDisplayName().charAt(0)));
//            contactViewHolder.vRoundLetterView.setBackgroundColor(contactItem.getBackgroundColor());

            if (contactItem.getPhoneNumbers().size() > 0) {
                String phoneNumber = contactItem.getPhoneNumbers().get(0).getNumber().replaceAll("\\s+", "");
                String displayName = contactItem.getDisplayName().replaceAll("\\s+", "");
                if (!phoneNumber.equals(displayName)) {
                    contactViewHolder.tvNumber.setVisibility(View.VISIBLE);
                    contactViewHolder.tvNumber.setText(phoneNumber);
                } else {
                    contactViewHolder.tvNumber.setVisibility(View.GONE);
                }
            } else {
                /*if (contactItem.getEmails().size() > 0) {
                    String email = contactItem.getEmails().get(0).replaceAll("\\s+", "");
                    String displayName = contactItem.getDisplayName().replaceAll("\\s+", "");
                    if (!email.equals(displayName)) {
                        contactViewHolder.tvNumber.setVisibility(View.VISIBLE);
                        contactViewHolder.tvNumber.setText(email);
                    } else {
                        contactViewHolder.tvNumber.setVisibility(View.GONE);
                    }
                } else {
                    contactViewHolder.tvNumber.setVisibility(View.GONE);
                }*/
            }

            highlightTerm(contactViewHolder.tvContactName, currentFilterQuery, contactViewHolder.tvContactName.getText().toString());

            if (contactItem.isSelected()) {
//                contactViewHolder.ivSelectedState.setVisibility(View.VISIBLE);

                contactViewHolder.ckb_name_selection.setChecked(true);

            } else {
//                contactViewHolder.ivSelectedState.setVisibility(View.INVISIBLE);
//                contactViewHolder.ll_main.setVisibility(View.INVISIBLE);
                contactViewHolder.ckb_name_selection.setChecked(false);
            }


           /* contactViewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag = contactItem.isSelected();
                    contactViewHolder.ckb_name_selection.setChecked(!flag);
                    isSelected[i] = !isSelected[i];
                }
            });*/
//
//            contactViewHolder.ckb_name_selection.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    isSelected[i] = !isSelected[i];
//                }
//            });
//
//

            contactViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContactSelected(contactItem.getId());
                    if (listener != null) {
                        listener.onContactSelected(getItem(i), getSelectedContactsCount());
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void highlightTerm(TextView tv, String query, String originalString) {
        if (query != null && !query.isEmpty()) {
            int startPos = originalString.toLowerCase().indexOf(query.toLowerCase());
            int endPos = startPos + query.length();
            if (startPos != -1) {
                Spannable spannable = new SpannableString(originalString);
                ColorStateList blackColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLACK});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blackColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(spannable);
            } else {
                tv.setText(originalString);
            }
        } else {
            tv.setText(originalString);
        }
    }

    protected void setAllSelected(boolean isAll) {
        for (Contact c : contactItemList) {
            c.setSelected(isAll);

            if (listener != null) {
                listener.onContactSelected(c, getSelectedContactsCount());
            }
        }
        notifyDataSetChanged();
    }

    protected void setContactSelected(long id) {
        int pos = getItemPosition(contactItemList, id);
        contactItemList.get(pos).setSelected(!contactItemList.get(pos).isSelected());
    }

    private int getItemPosition(List<Contact> list, long mid) {
        int i = 0;
        for (Contact contact : list) {
            if (contact.getId() == mid) {
                return i;
            }
            i++;
        }
        return -1;
    }

    protected int getSelectedContactsCount() {
        return ((getSelectedContacts() != null) ? getSelectedContacts().size() : 0);
    }

    List<Contact> getSelectedContacts() {
        List<Contact> selectedContacts = new ArrayList<>();
        for (Contact contact : contactItemListOriginal) {
            if (contact.isSelected()) {
                selectedContacts.add(contact);
            }
        }
        return selectedContacts;
    }

    @Override
    public int getItemCount() {
        return (null != contactItemList ? contactItemList.size() : 0);
    }

    private Contact getItem(int pos) {
        return contactItemList.get(pos);
    }


    private class ContactViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private TextView tvContactName;
        private TextView tvNumber;
        LinearLayout ll_main;
        CheckBox ckb_name_selection;
//        private RoundLetterView vRoundLetterView;

        ContactViewHolder(View view) {
            super(view);
            this.mView = view;
//            this.vRoundLetterView = (RoundLetterView) view.findViewById(R.id.vRoundLetterView);
            this.tvContactName = (TextView) view.findViewById(R.id.tvContactName);
            this.tvNumber = (TextView) view.findViewById(R.id.tvNumber);
            this.ll_main = view.findViewById(R.id.ll_main);
            this.ckb_name_selection = view.findViewById(R.id.ckb_name_selection);
//            this.ivSelectedState = (ImageView) view.findViewById(R.id.ivSelectedState);
        }
    }

    public void filterOnText(String query) {
        this.currentFilterQuery = query;
        getFilter().filter(currentFilterQuery);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactItemList = (List<Contact>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Contact> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = contactItemListOriginal;
                    currentFilterQuery = null;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

    private List<Contact> getFilteredResults(String constraint) {
        List<Contact> results = new ArrayList<>();
        for (Contact item : contactItemListOriginal) {
            if (item.getDisplayName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
    public boolean[] getSelectedFlags() {
        return isSelected;
    }
}