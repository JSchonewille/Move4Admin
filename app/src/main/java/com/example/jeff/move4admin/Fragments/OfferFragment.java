package com.example.jeff.move4admin.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4admin.Library.Like;
import com.example.jeff.move4admin.Library.Offer;
import com.example.jeff.move4admin.Library.Product;
import com.example.jeff.move4admin.Library.ServerLoader;
import com.example.jeff.move4admin.Library.ServerRequestHandler;
import com.example.jeff.move4admin.Library.adapters.AllCategoriesAdapter;
import com.example.jeff.move4admin.Library.adapters.OfferAdapter;
import com.example.jeff.move4admin.Library.adapters.ProductAdapter;
import com.example.jeff.move4admin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class OfferFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Menu menu;
    private MenuItem m_add;
    private MenuItem m_save;
    private MenuItem m_edit;

    private ArrayList<Like> categoriesList = new ArrayList<Like>();
    private ArrayList<Offer> offerList = new ArrayList<Offer>();
    private Context mContext;
    private LinearLayout lin_offerTitle;
    private LinearLayout lin_offerEditTitle;
    private FrameLayout f_viewFrame;
    private FrameLayout f_editFrame;

    private ListView l_offerListView;
    private ImageView i_offerImage;
    private ImageView i_addOfferImage;
    private TextView t_offerNameLabel;
    private TextView t_offerDesc;
    private TextView t_offerCategory;
    private TextView t_offerCategoryLabel;
    private TextView t_offerIDLabel;
    private TextView t_addOfferLabel;
    private Spinner s_offerCategory;
    private EditText e_offerDesc;
    private String savedPath;
    private ContextWrapper cw;

    private OnFragmentInteractionListener mListener;

    public OfferFragment() {
        // Required empty public constructor
    }

    public static OfferFragment newInstance(String param1, String param2) {
        OfferFragment fragment = new OfferFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
        this.menu = menu;
        m_add = menu.findItem(R.id.action_add);
        m_save = menu.findItem(R.id.action_save);
        m_edit = menu.findItem(R.id.action_edit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                // reset the labels we use for showing when we edit something
                t_addOfferLabel.setText("");
                t_addOfferLabel.setVisibility(View.VISIBLE);
                // sets the buttons
                f_viewFrame.setVisibility(View.GONE);
                f_editFrame.setVisibility(View.VISIBLE);
                m_add.setVisible(false);
                m_edit.setVisible(false);
                m_save.setVisible(true);

                // resets the layout

                s_offerCategory.setSelection(0);
                i_addOfferImage.setImageResource(R.drawable.no_product);
                return true;
            case R.id.action_save:
                final String desc = e_offerDesc.getText().toString();
                if ((t_addOfferLabel.getText().length() > 0) && (t_addOfferLabel.getVisibility() == View.VISIBLE)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Overwrite");
                    alert.setMessage("Do you want to overwrite this offer?");
                    // Set an EditText view to get user input
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Bitmap b = ((BitmapDrawable) i_addOfferImage.getDrawable()).getBitmap();
                            b = Bitmap.createScaledBitmap(b, 200, 200, false);
                            String img = base64(i_addOfferImage.getDrawable());
                            int categoryid = categoriesList.get(s_offerCategory.getSelectedItemPosition()).getcategoryID();
                            f_editFrame.setVisibility(View.GONE);
                            f_viewFrame.setVisibility(View.VISIBLE);
                            m_save.setVisible(false);
                            saveOffer(Integer.valueOf(t_addOfferLabel.getText().toString()),categoryid, img, desc, b);
                        }
                    });
                    alert.show();
                    break;
                }
                else
                {
                    Bitmap b = ((BitmapDrawable) i_addOfferImage.getDrawable()).getBitmap();
                    b = Bitmap.createScaledBitmap(b, 200, 200, false);
                    String img = base64(i_addOfferImage.getDrawable());
                    int categoryid = categoriesList.get(s_offerCategory.getSelectedItemPosition()).getcategoryID();
                    f_editFrame.setVisibility(View.GONE);
                    f_viewFrame.setVisibility(View.VISIBLE);
                    m_save.setVisible(false);
                    saveOffer(categoryid, img, desc, b);
                }
                return true;


            case R.id.action_edit:
                // sets the buttons
                f_viewFrame.setVisibility(View.GONE);
                f_editFrame.setVisibility(View.VISIBLE);
                m_add.setVisible(false);
                m_edit.setVisible(false);
                m_save.setVisible(true);

                // resets the layout
                t_addOfferLabel.setText(t_offerIDLabel.getText());
                t_addOfferLabel.setVisibility(View.VISIBLE);
                lin_offerEditTitle.setBackgroundResource(R.color.move4darker);
                int position = 0;
                for (Like c : categoriesList) {
                    if (c.getcategoryName().equals(t_offerCategory.getText().toString())) {
                        s_offerCategory.setSelection(position);
                        break;
                    }
                    position++;
                }
                i_addOfferImage.setImageDrawable(i_offerImage.getDrawable());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_offer, container, false);
        cw = new ContextWrapper(getActivity());
        savedPath = cw.getDir("imageDir", Context.MODE_PRIVATE).toString();
        /////////////////////// view///////////////////////////////////////
        f_viewFrame = (FrameLayout) v.findViewById(R.id.offerViewFrame);
        f_editFrame = (FrameLayout) v.findViewById(R.id.offerEditFrame);
        l_offerListView = (ListView) v.findViewById(R.id.offerList);
        i_offerImage = (ImageView) v.findViewById(R.id.i_offerImage);
        t_offerNameLabel = (TextView) v.findViewById(R.id.t_offerLabel);
        t_offerDesc = (TextView) v.findViewById(R.id.t_offerInfo);
        t_offerCategory = (TextView) v.findViewById(R.id.t_offerCategory);
        t_offerCategoryLabel = (TextView) v.findViewById(R.id.t_offerCategoryLabel);
        t_offerIDLabel = (TextView) v.findViewById(R.id.t_offerIDLabel);
        lin_offerTitle = (LinearLayout) v.findViewById(R.id.l_offerViewTitle);
        lin_offerEditTitle = (LinearLayout) v.findViewById(R.id.l_offerEditTitle);
        ///////////////////////////////////////////////////////////////////

        /////////////////////// edit//////////////////////////////////////
        e_offerDesc = (EditText) v.findViewById(R.id.e_addOfferDesc);
        t_addOfferLabel = (TextView) v.findViewById(R.id.t_addOfferLabel);
        s_offerCategory = (Spinner) v.findViewById(R.id.s_addOfferCategory);
        i_addOfferImage = (ImageView) v.findViewById(R.id.i_OfferaddImage);
        i_addOfferImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        //////////////////////////////////////////////////////////////////
        getCategories();
        f_editFrame.setVisibility(View.GONE);
        f_viewFrame.setVisibility(View.VISIBLE);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOfferInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_CANCELED = 0;
        int RESULT_OK = -1;
        int RESULT_FIRST_USER = 1;

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(cw.getContentResolver().openInputStream(targetUri));
                Bitmap b = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                i_addOfferImage.setImageBitmap(b);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
       super.onDetach();
        mListener = null;
    }

    private void getOffers() {
        ServerRequestHandler.getAllOffers(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                offerList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject o = jsonArray.getJSONObject(i);
                        Offer offer = new Offer();
                        int id = o.getInt("id");
                        int cat;
                        String image;
                        String desc;

                        if (!o.isNull("category")) {
                            cat = o.getInt("category");
                            offer.setCategoryID(cat);
                        }

                        if (!o.isNull("image")) {
                            image = o.getString("image");
                            offer.setImage(image);
                        }

                        if (!o.isNull("description")) {
                            desc = o.getString("description");
                            offer.setDescription(desc);
                        }

                        offer.setOfferID(id);


                        offerList.add(offer);

                    } catch (Exception e) {
                        Log.e("error", e.toString());
                    }
                }
                // set adapter after filling the list of offers
                if (l_offerListView != null) {

                    l_offerListView.setAdapter(new OfferAdapter(mContext, offerList));
                    l_offerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // function responsible for the clicks in the offer list
                            offerListClick(adapterView, view, i, l);
                            f_viewFrame.setVisibility(View.VISIBLE);
                            f_editFrame.setVisibility(View.GONE);
                            m_save.setVisible(false);
                            m_add.setVisible(true);
                        }
                    });
                    l_offerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // function responsible for the product list long clicks
                            offerListLongClick(adapterView, view, i, l);
                            return true;
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", volleyError.toString());

            }
        }, mContext);
    }

    public void offerListLongClick(AdapterView<?> arg0, final View arg1,
                                     int position, long arg3) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Delete");
        alert.setMessage("Do you want to delete this offer ?");
        // Set an EditText view to get user input
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                TextView txtview = (TextView) arg1.findViewById(R.id.rowLayoutName);
                final String text = txtview.getText().toString();
                ServerRequestHandler.DeleteOffer(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String s = jsonObject.getString("returnvalue");
                            if (s.equals("succes")) {
                                getOffers();
                            } else {
                                Log.e("error", s);
                                Log.e("text = ", text);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }, text, mContext);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void offerListClick(AdapterView<?> adapterView, View view, int Position, long id) {
        Offer o = offerList.get(Position);
        t_offerIDLabel.setText(Integer.toString(o.getOfferID()));
        if(o.getDescription().length()> 40) {
            t_offerNameLabel.setText(o.getDescription().substring(0, 40) + "...");
        }
        else
        {
            t_offerNameLabel.setText(o.getDescription());
        }
        for (Like c : categoriesList) {
            if (c.getcategoryID() == o.getCategoryID()) {
                t_offerCategory.setText(c.getcategoryName());
                t_offerCategoryLabel.setVisibility(View.VISIBLE);
                break;
            }
        }
        m_edit.setVisible(true);
        lin_offerTitle.setBackgroundResource(R.color.move4darker);
        t_offerNameLabel.setVisibility(View.VISIBLE);
        t_offerIDLabel.setVisibility(View.VISIBLE);
        t_offerDesc.setText(o.getDescription());

        try {
            File f = new File(savedPath, o.getImage().substring(7));
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            b = Bitmap.createScaledBitmap(b, 800, 800, true);
            i_offerImage.setImageBitmap(b);
        } catch (Exception e) {
            Bitmap noimg = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.no_product);
            noimg = Bitmap.createScaledBitmap(noimg, 800, 800, true);
            i_offerImage.setImageBitmap(noimg);
        }
    }

    public void getCategories() {
        final ArrayList<Like> categories = new ArrayList<Like>();
        ServerRequestHandler.getAllCategories(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject o = jsonArray.getJSONObject(i);

                        int id = o.getInt("id");
                        String name = o.getString("name");

                        Like l = new Like(id, name);
                        categories.add(l);
                    } catch (Exception e) {

                    }
                }
                AllCategoriesAdapter ac = new AllCategoriesAdapter(mContext, categories);
                s_offerCategory.setAdapter(ac);
                categoriesList = categories;
                getOffers();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mContext);
    }

    public void saveOffer( final int categoryID, final String image, final String desc, final Bitmap bitmap) {

        ServerRequestHandler.uploadOffer(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("returnvalue", jsonObject.getString("returnvalue"));
                    String filepath = jsonObject.getString("returnvalue");
                    ServerLoader.getInstance(mContext).saveToInternalSorage(bitmap, filepath);
                    getOffers();


                    for (Like c : categoriesList) {
                        if (c.getcategoryID() == categoryID) {
                            t_offerCategory.setText(c.getcategoryName());
                            break;
                        }
                    }
                    t_offerDesc.setText(desc);
                    //t_offerNameLabel.setText(name);
                    i_offerImage.setImageBitmap(bitmap);
                    m_add.setVisible(true);
                    m_edit.setVisible(true);
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("error", volleyError.toString());
            }
        }, categoryID, image, desc, mContext);
    }


    public void saveOffer( final int productID,final int categoryID, final String image, final String desc, final Bitmap bitmap) {

        ServerRequestHandler.uploadOffer(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.e("returnvalue", jsonObject.getString("returnvalue"));
                    String filepath = jsonObject.getString("returnvalue");
                    ServerLoader.getInstance(mContext).saveToInternalSorage(bitmap, filepath);
                    getOffers();


                    for (Like c : categoriesList) {
                        if (c.getcategoryID() == categoryID) {
                            t_offerCategory.setText(c.getcategoryName());
                            break;
                        }
                    }
                    t_offerDesc.setText(desc);
                    //t_offerNameLabel.setText(name);
                    i_offerImage.setImageBitmap(bitmap);
                    m_add.setVisible(true);
                    m_edit.setVisible(true);
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("error", volleyError.toString());
            }
        },productID, categoryID, image, desc, mContext);
    }

    public String base64(Drawable d) {
        Bitmap b = ((BitmapDrawable) d).getBitmap();
        b = Bitmap.createScaledBitmap(b, 200, 200, false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return Base64.encodeToString(bitmapdata, 1);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onOfferInteraction(Uri uri);
    }

}

