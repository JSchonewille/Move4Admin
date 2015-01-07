package com.example.jeff.move4admin.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4admin.Library.Beacon;
import com.example.jeff.move4admin.Library.BeaconDrawable;
import com.example.jeff.move4admin.Library.DatabaseFunctions;
import com.example.jeff.move4admin.Library.Like;
import com.example.jeff.move4admin.Library.Offer;
import com.example.jeff.move4admin.Library.Product;
import com.example.jeff.move4admin.Library.ServerRequestHandler;
import com.example.jeff.move4admin.Library.adapters.BeaconAdapter;
import com.example.jeff.move4admin.Library.adapters.OfferAdapter;
import com.example.jeff.move4admin.Library.adapters.ProductAdapter;
import com.example.jeff.move4admin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BeaconFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeaconFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeaconFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Menu menu;
    private MenuItem m_add;
    private MenuItem m_info;
    private MenuItem m_save;
    private MenuItem m_edit;
    private MenuItem m_options;
    private Context mContext;
    private ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
    private ArrayList<BeaconDrawable> screenBeaconList = new ArrayList<BeaconDrawable>();
    private ArrayList<Offer> offerList = new ArrayList<Offer>();
    private ArrayList<Product> productList = new ArrayList<Product>();
    private ArrayList<Like> categoryList = new ArrayList<Like>();
    private BeaconAdapter beaconAdapter;
    private Boolean infoscreenOpen = false;
    private Boolean initDone = false;
    private Boolean firstSelection = false;
    private Boolean editaction = false;
    private String savedPath;

    private Button b_infoClose;
    private Button b_editClose;
    // all infoscreen variables
    private TextView t_infoMajor;
    private TextView t_infoMinor;
    private TextView t_infoProductID;
    private TextView t_infoOfferID;
    private TextView t_infoProductName;
    private TextView t_infoProductCategory;
    private TextView t_infoProductDesc;
    private TextView t_infoOfferCategory;
    private TextView t_infoOfferDesc;
    private ImageView i_infoProductImage;
    private ImageView i_infoOfferImage;

    //all edit screen variables
    private TextView t_editMajor;
    private TextView t_editoMinor;
    private TextView t_editProductID;
    private TextView t_editOfferID;
    private TextView t_editProductName;
    private TextView t_editProductCategory;
    private TextView t_editProductDesc;
    private TextView t_editOfferCategory;
    private TextView t_editOfferDesc;
    private ImageView i_editProductImage;
    private ImageView i_editOfferImage;
    private EditText e_editMajor;
    private EditText e_editMinor;
    private Spinner s_editProductSpinner;
    private Spinner s_editOfferSpinner;

    private ListView l_beaconListView;
    private ImageView i_star;
    private FrameLayout drawFrame;
    private FrameLayout slideInFrame;
    private LinearLayout infoLinearLayout;
    private LinearLayout editLinearLayout;
    private ContextWrapper cw;

    private OnFragmentInteractionListener mListener;

    public BeaconFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BeaconFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeaconFragment newInstance(String param1, String param2) {
        BeaconFragment fragment = new BeaconFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContext = getActivity();
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                editaction = false;
                infoLinearLayout.setVisibility(View.GONE);
                editLinearLayout.setVisibility(View.VISIBLE);
                l_beaconListView.setEnabled(false);
                m_options.setVisible(false);
                m_edit.setVisible(false);
                m_info.setVisible(false);
                m_add.setVisible(false);
                m_save.setVisible(true);
                e_editMinor.setEnabled(true);
                e_editMajor.setEnabled(true);
                clearSelection();
                clearEdit();
                showSlide();
                return true;
            case R.id.action_info:
                l_beaconListView.setEnabled(true);
                m_options.setVisible(false);
                editaction = false;
                editLinearLayout.setVisibility(View.GONE);
                infoLinearLayout.setVisibility(View.VISIBLE);
                showSlide();
                return true;
            case R.id.action_save:
                save();
                l_beaconListView.setEnabled(true);
                return true;
            case R.id.action_edit:
                editaction = true;
                l_beaconListView.setEnabled(true);
                m_options.setVisible(false);
                m_edit.setVisible(false);
                m_info.setVisible(false);
                m_add.setVisible(false);
                m_save.setVisible(true);
                infoLinearLayout.setVisibility(View.GONE);
                editLinearLayout.setVisibility(View.VISIBLE);
                e_editMinor.setEnabled(false);
                e_editMajor.setEnabled(false);
                showSlide();
                return true;
            case R.id.action_options:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when selecting an image
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
                BitmapDrawable dr = new BitmapDrawable(getResources(), b);
                drawFrame.setBackground(dr);
                Time t = new Time();
                t.setToNow();
                String filepath = t.format3339(false) + ".jpg";
                Log.e("filepath", filepath);
                saveToInternalSorage(b, filepath);
                DatabaseFunctions.getInstance(mContext).addBeaconBackground(filepath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
        this.menu = menu;
        m_add = menu.findItem(R.id.action_add);
        m_save = menu.findItem(R.id.action_save);
        m_edit = menu.findItem(R.id.action_edit);
        m_options = menu.findItem(R.id.action_options);
        m_info = menu.findItem(R.id.action_info);
        m_options.setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cw = new ContextWrapper(getActivity());
        savedPath = cw.getDir("imageDir", Context.MODE_PRIVATE).toString();
        final View v = inflater.inflate(R.layout.fragment_beacon, container, false);
        cw = new ContextWrapper(getActivity());
        i_star = (ImageView) v.findViewById(R.id.i_star);
        l_beaconListView = (ListView) v.findViewById(R.id.beaconList);
        drawFrame = (FrameLayout) v.findViewById(R.id.f_starFrame);
        slideInFrame = (FrameLayout) v.findViewById(R.id.f_beaconSlideinSceen);
        slideInFrame.setVisibility(View.GONE);
        b_infoClose = (Button) v.findViewById(R.id.b_infoClose);
        b_editClose = (Button) v.findViewById(R.id.b_beaconEditClose);
        infoLinearLayout = (LinearLayout) v.findViewById(R.id.f_BeaconInfoFrame);
        editLinearLayout = (LinearLayout) v.findViewById(R.id.f_BeaconEditFrame);
        infoLinearLayout.setVisibility(View.GONE);
        editLinearLayout.setVisibility(View.GONE);
        // info variables intialisation
        t_infoMajor = (TextView) v.findViewById(R.id.t_infoMajorLabel);
        t_infoMinor = (TextView) v.findViewById(R.id.t_infoMinorLabel);
        t_infoOfferID = (TextView) v.findViewById(R.id.t_infoOfferIDLabel);
        t_infoProductID = (TextView) v.findViewById(R.id.t_infoProductIDLabel);
        t_infoProductName = (TextView) v.findViewById(R.id.t_infoProductNameLabel);
        t_infoProductCategory = (TextView) v.findViewById(R.id.t_infoProductCategory);
        t_infoProductDesc = (TextView) v.findViewById(R.id.t_infoProductDescription);
        t_infoOfferCategory = (TextView) v.findViewById(R.id.t_infoOfferCategory);
        t_infoOfferDesc = (TextView) v.findViewById(R.id.t_infoOfferDesc);
        i_infoProductImage = (ImageView) v.findViewById(R.id.i_infoProductImage);
        i_infoOfferImage = (ImageView) v.findViewById(R.id.i_infoOfferImage);
        // edit variables initialisation
        t_editMajor = (TextView) v.findViewById(R.id.t_editMajorLabel);
        t_editoMinor = (TextView) v.findViewById(R.id.t_editMinorLabel);
        t_editProductID = (TextView) v.findViewById(R.id.t_editProductIDLabel);
        t_editOfferID = (TextView) v.findViewById(R.id.t_editOfferIDLabel);
        t_editProductName = (TextView) v.findViewById(R.id.t_editProductNameLabel);
        t_editProductCategory = (TextView) v.findViewById(R.id.t_editProductCategory);
        t_editProductDesc = (TextView) v.findViewById(R.id.t_editProductDescription);
        t_editOfferCategory = (TextView) v.findViewById(R.id.t_editOfferCategory);
        t_editOfferDesc = (TextView) v.findViewById(R.id.t_editOfferDesc);
        e_editMajor = (EditText) v.findViewById(R.id.e_editMajor);
        e_editMinor = (EditText) v.findViewById(R.id.e_editMinor);
        s_editProductSpinner = (Spinner) v.findViewById(R.id.s_editProductSpinner);
        s_editOfferSpinner = (Spinner) v.findViewById(R.id.s_editOfferSpinner);

        String beaconsFrameBackground = DatabaseFunctions.getInstance(mContext).getBeaconBackground();

        if (beaconsFrameBackground.length() > 5) {
            try {
                File f = new File(cw.getDir("imageDir", Context.MODE_PRIVATE).toString(), beaconsFrameBackground);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                BitmapDrawable dr = new BitmapDrawable(getResources(), b);
                drawFrame.setBackground(dr);
            } catch (FileNotFoundException e) {
                Log.e("file not found", "could not find file " + beaconsFrameBackground);
            }
        }
        drawFrame.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                return viewDrag(view, dragEvent);
            }
        });

        l_beaconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                beaconListClick(adapterView, view, i, l);
            }
        });

        l_beaconListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                beaconListLongClick(adapterView, view,i,l);
                return true;
            }
        });

        s_editOfferSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView3, View view3, int i3, long l3) {
                editOfferSpinnerClick(adapterView3,view3,i3,l3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        s_editProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView4, View view4, int i4, long l4) {
                editProductSpinnerClick(adapterView4,view4,i4,l4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        b_infoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSlide();
            }
        });
        b_editClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSlide();
            }
        });
        initProductOffer();
        getbeacons();


        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBeaconInteraction(uri);
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

    private void viewClick(View v) {
        int i = 0;
        for (BeaconDrawable bd : screenBeaconList) {
            if (bd.getImageView().getTag().equals(v.getTag())) {
                setSelection(i);
                break;
            }
            i++;
        }

    }

    private boolean viewLongClick(View view) {

        int i = 0;
        for (BeaconDrawable bd : screenBeaconList) {
            if (bd.getImageView().getTag().equals(view.getTag())) {
                setSelection(i);
                break;
            }
            i++;
        }

        // create it from the object's tag
        ClipData.Item item = new ClipData.Item("");
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, //data to be dragged
                shadowBuilder, //drag shadow
                view,
                0 //local data about the drag and drop operation
                //no needed flags

        );
        view.setVisibility(View.INVISIBLE);
        return true;

    }

    private boolean viewDrag(View v, DragEvent event) {
        // Handles each of the expected events

        switch (event.getAction()) {
            //signal for the start of a drag and drop operation.
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            //the drag point has entered the bounding box of the View
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            //the user has moved the drag shadow outside the bounding box of the View
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            //drag shadow has been released,the drag point is within the bounding box of the View
            case DragEvent.ACTION_DROP:
                //we accept the drag item
                View view = (View) event.getLocalState();
                view.setVisibility(View.GONE);
                // get the parent of our group
                FrameLayout containView = (FrameLayout) v;
                //remove the old view
                ViewGroup viewParent = (ViewGroup) view.getParent();
                viewParent.removeView(view);
                // cast out parent to a framelayout
                view.setX(event.getX());
                view.setY(event.getY());
                for (BeaconDrawable bd : screenBeaconList) {
                    if (bd.getImageView().equals(view)) {
                        // save to local database
                        Log.e("Beacon selected", Integer.toString(bd.getMinor()) + "-" + Float.toString(event.getX()) + "  -  " + Float.toString(event.getY()));
                        DatabaseFunctions.getInstance(mContext).addBeaconLocation(bd.getMajor(), bd.getMinor(), Math.round(event.getX()), Math.round(event.getY()));
                    }
                }

                //DatabaseFunctions.getInstance(mContext).addBeaconLocation();
                containView.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            //the drag and drop operation has concluded.
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            default:
                break;
        }
        return true;

    }

    public void beaconListClick(AdapterView<?> adapterView, View view, int Position, long id) {
        setSelection(Position);
    }

    private boolean beaconListLongClick(AdapterView<?> adapterView2, View view2, final int position2, long l2)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Delete");
        alert.setMessage("Do you want to delete this beacon ?");
        // Set an EditText view to get user input
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Beacon ins = beaconList.get(position2);
                deleteBeacon(ins.getBeaconID(),ins.getMajor(),ins.getMinor(),position2);
            }
        });
        alert.show();

        return true;
    }

    public void getbeacons() {


        ServerRequestHandler.getAllBeacons(new Response.Listener<JSONArray>() {
            float starx = 100;
            float stary = 100;

            @Override
            public void onResponse(JSONArray jsonArray) {
                beaconList.clear();
                screenBeaconList.clear();
                screenBeaconList = DatabaseFunctions.getInstance(mContext).getBeaconLocations();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject o = jsonArray.getJSONObject(i);
                        Beacon beacon = new Beacon();
                        int beaconID = o.getInt("beaconID");
                        int minor = o.getInt("minor");
                        int major = o.getInt("major");
                        int offerID;
                        int productID;
                        if (!o.isNull("offerID")) {
                            offerID = o.getInt("offerID");
                            beacon.setOfferID(offerID);
                        }

                        if (!o.isNull("productID")) {
                            productID = o.getInt("productID");
                            beacon.setProductID(productID);
                        }
                        beacon.setBeaconID(beaconID);
                        beacon.setMajor(major);
                        beacon.setMinor(minor);

                        beaconList.add(beacon);

                        // make the dynamic image views
                        ImageView iv = new ImageView(mContext);
                        iv.setImageDrawable(i_star.getDrawable());
                        iv.setScaleType(i_star.getScaleType());
                        iv.setLayoutParams(i_star.getLayoutParams());
                        iv.setTag(i);
                        iv.setY(stary);
                        iv.setX(starx);
                        // set the drag action
                        iv.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                return viewLongClick(view);
                            }
                        });
                        // set the click action
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewClick(view);
                            }
                        });

                        boolean found = false;
                        // check if our beaconposition was already saved, if so we load that data
                        for (BeaconDrawable bdl : screenBeaconList) {
                            if (bdl.getMajor() == major && bdl.getMinor() == minor) {
                                iv.setX(bdl.getX());
                                iv.setY(bdl.getY());
                                bdl.setImageView(iv);
                                found = true;
                                break;

                            }
                        }
                        if (!found) {
                            BeaconDrawable bd = new BeaconDrawable();
                            bd.setMinor(minor);
                            bd.setMajor(major);
                            bd.setImageView(iv);
                            screenBeaconList.add(bd);
                        }
                        drawFrame.addView(iv);
                        starx += 50;
                        stary += 50;

                    } catch (Exception e) {
                        Log.e("error", e.toString());
                    }
                    // set adapter after filling our list of offers
                    if (l_beaconListView != null) {
                        beaconAdapter = new BeaconAdapter(mContext, beaconList);
                        l_beaconListView.setAdapter(beaconAdapter);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mContext);
    }

    public void saveToInternalSorage(Bitmap bitmapImage, String filename) {
        ContextWrapper cw = new ContextWrapper(mContext);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir we remove the images/ by usign substring
        File mypath = new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSlide() {

        if (!infoscreenOpen) {
            AnimationSet set = new AnimationSet(true);
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.enter_from_right);

            animation.setDuration(400);
            set.addAnimation(animation);

            slideInFrame.setVisibility(View.VISIBLE);
            slideInFrame.startAnimation(animation);
            infoscreenOpen = true;
        }

    }

    public void hideSlide() {
        if (infoscreenOpen) {
            m_add.setVisible(true);
            m_options.setVisible(true);
            m_save.setVisible(false);
            l_beaconListView.setEnabled(true);
            if (editaction)
            {
                m_info.setVisible(true);
                m_edit.setVisible(true);
            }

            AnimationSet set = new AnimationSet(true);
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.exit_to_right);

            animation.setDuration(400);
            set.addAnimation(animation);

            slideInFrame.setVisibility(View.GONE);
            slideInFrame.startAnimation(animation);
            infoscreenOpen = false;
        }

    }

    public void setSelection(int input) {
        // this function sets our list on selected and sets the image
        firstSelection = true;
        if (initDone && !editaction) {
            m_info.setVisible(true);
            m_edit.setVisible(true);
        }
        int length = beaconList.size();


        for (int i = 0; i < length; i++) {
            if (i == input) {
                l_beaconListView.getChildAt(i).setBackgroundResource(android.R.color.holo_blue_dark);
                screenBeaconList.get(i).getImageView().setImageResource(R.drawable.ibeacon_on);
                // setting the info of the selected beacon
                setInfo(i);
                setEdit(i);
            } else {
                l_beaconListView.getChildAt(i).setBackgroundResource(android.R.color.transparent);
                screenBeaconList.get(i).getImageView().setImageResource(R.drawable.ibeacon_off);
            }
        }

    }

    public void clearSelection() {
        // this function sets our list on selected and sets the image
        int length = beaconList.size();
        for (int i = 0; i < length; i++) {
            l_beaconListView.getChildAt(i).setBackgroundResource(android.R.color.transparent);
            screenBeaconList.get(i).getImageView().setImageResource(R.drawable.ibeacon_off);
        }
        m_edit.setVisible(false);
    }

    public void initProductOffer() {
        ServerRequestHandler.getAllProducts(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                productList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject o = jsonArray.getJSONObject(i);
                        Product p = new Product();
                        int id = o.getInt("id");
                        String name = o.getString("name");
                        int cat;
                        String image;
                        String desc;

                        if (!o.isNull("categoryID")) {
                            cat = o.getInt("categoryID");
                            p.setCategoryID(cat);
                        }

                        if (!o.isNull("image")) {
                            image = o.getString("image");
                            p.setImage(image);
                        }

                        if (!o.isNull("description")) {
                            desc = o.getString("description");
                            p.setDescription(desc);
                        }

                        p.setProductID(id);
                        p.setName(name);

                        productList.add(p);

                    } catch (Exception e) {
                        Log.e("error", e.toString());
                    }
                }
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

                        ServerRequestHandler.getAllCategories(new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject o = jsonArray.getJSONObject(i);
                                        int id = o.getInt("id");
                                        String like = o.getString("name");
                                        Like l = new Like(id, like);
                                        categoryList.add(l);
                                    } catch (Exception e) {

                                    }
                                }
                                // logica for when everything has loaded
                                initDone = true;
                                m_add.setVisible(true);
                                if (firstSelection) {
                                    m_edit.setVisible(true);
                                    m_info.setVisible(true);

                                }
                                s_editProductSpinner.setAdapter(new ProductAdapter(mContext, productList));
//                                s_editProductSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                        editProductSpinnerClick(adapterView,view,i,l);
//                                    }
//                                });
                                s_editOfferSpinner.setAdapter(new OfferAdapter(mContext, offerList));
//                                s_editOfferSpinner.setOnItemSelectedListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                        editOfferSpinnerClick(adapterView,view,i,l);
//                                    }
//                                });

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }, mContext);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }, mContext);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mContext);
    }

    public void setInfo(int input) {
        if (initDone) {
            Beacon b = beaconList.get(input);
            Offer beaconOffer = new Offer();
            Product beaconProduct = new Product();
            String offerCategory = "";
            String productCategory = "";

            for (Offer of : offerList) {
                if (of.getOfferID() == b.getOfferID()) {
                    beaconOffer = of;
                    for (Like cat : categoryList) {
                        if (cat.getcategoryID() == of.getCategoryID()) {
                            offerCategory = cat.getcategoryName();
                            break;
                        }
                    }
                    break;
                }
            }
            for (Product pr : productList) {
                if (pr.getProductID() == b.getProductID()) {
                    beaconProduct = pr;
                    for (Like l : categoryList) {
                        if (l.getcategoryID() == pr.getCategoryID()) {
                            productCategory = l.getcategoryName();
                        }
                    }
                    break;
                }
            }
            int major = b.getMajor();
            int minor = b.getMinor();
            int OfferID = b.getOfferID();
            int productID = b.getProductID();

            t_infoMajor.setText("Major: " + Integer.toString(major));
            t_infoMinor.setText("Minor: " + Integer.toString(minor));
            t_infoProductID.setText("Product id: " + Integer.toString(productID));
            t_infoOfferID.setText("Offer id: " + Integer.toString(OfferID));
            t_infoProductName.setText("Name: " + beaconProduct.getName());
            t_infoProductCategory.setText("Category: " + productCategory);
            t_infoOfferCategory.setText("Category: " + offerCategory);
            t_infoProductDesc.setText(beaconProduct.getDescription());
            t_infoOfferDesc.setText(beaconOffer.getDescription());
            ;
            try {
                File f = new File(savedPath, beaconProduct.getImage().substring(7));
                Bitmap b1 = BitmapFactory.decodeStream(new FileInputStream(f));
                //b1 = Bitmap.createScaledBitmap(b1, 800, 800, true);
                i_infoProductImage.setImageBitmap(b1);
            } catch (Exception e) {
                Bitmap noimg1 = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.no_product);
                //noimg1 = Bitmap.createScaledBitmap(noimg1, 800, 800, true);
                i_infoProductImage.setImageBitmap(noimg1);
            }

            try {
                File f = new File(savedPath, beaconOffer.getImage().substring(7));
                Bitmap b2 = BitmapFactory.decodeStream(new FileInputStream(f));
                // b2 = Bitmap.createScaledBitmap(b2, 800, 800, true);
                i_infoOfferImage.setImageBitmap(b2);
            } catch (Exception e) {
                Bitmap noimg2 = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.no_product);
                //noimg2 = Bitmap.createScaledBitmap(noimg2, 800, 800, true);
                i_infoOfferImage.setImageBitmap(noimg2);
            }

        }
    }

    public void setEdit(int input) {
        Beacon b = beaconList.get(input);
        int i =0;
        for (Offer o : offerList)
        {
            if (o.getOfferID() == b.getOfferID())
            {
              s_editOfferSpinner.setSelection(i);
            }
            i++;
        }

        int j = 0;
        for (Product p : productList)
        {
            if(p.getProductID() == b.getProductID())
            {
                s_editProductSpinner.setSelection(j);
            }
            j++;
        }


        e_editMajor.setText(Integer.toString(b.getMajor()));
        e_editMinor.setText(Integer.toString(b.getMinor()));



    }

    public void editProductSpinnerClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product p = productList.get(i);
        for (Like c : categoryList)
        {
            if (c.getcategoryID() == p.getCategoryID())
            {
                t_editProductCategory.setText("Category: " +c.getcategoryName());
            }
        }
        t_editProductName.setText("Name: " + p.getName());
        t_editProductDesc.setText(p.getDescription());
    }

    public void editOfferSpinnerClick(AdapterView<?> adapterView, View view, int i, long l) {
        Offer o = offerList.get(i);
        for (Like c : categoryList)
        {
            if (c.getcategoryID() == o.getCategoryID())
            {
                t_editOfferCategory.setText("Category: " +c.getcategoryName());
            }
        }
        t_editOfferDesc.setText(o.getDescription());

    }

    public void clearEdit() {
        e_editMajor.setText("31690");
        e_editMinor.setText("");
    }

    private void save() {
        if (!editaction) {
            if (e_editMajor.getText().length() > 0 && e_editMinor.getText().length() > 0) {
                int major = Integer.decode(e_editMajor.getText().toString());
                int minor = Integer.decode(e_editMinor.getText().toString());
                int productID = productList.get(s_editProductSpinner.getSelectedItemPosition()).getProductID();
                int offerID = offerList.get(s_editOfferSpinner.getSelectedItemPosition()).getOfferID();

                for (Beacon b : beaconList) {
                    // check if the combination already exists
                    if (b.getMajor() == major && b.getMinor() == minor) {
                        Toast.makeText(mContext, "Major / Minor combination already exists ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                insertBeacon(productID, offerID, major, minor);
            }
            else {
                Toast.makeText(mContext, "No value for Major or Minor ", Toast.LENGTH_SHORT).show();
            }
        }

      else if(editaction)
        {
            if (e_editMajor.getText().length() > 0 && e_editMinor.getText().length() > 0) {


                int major = Integer.decode(e_editMajor.getText().toString());
                int minor = Integer.decode(e_editMinor.getText().toString());
                int productID = productList.get(s_editProductSpinner.getSelectedItemPosition()).getProductID();
                int offerID = offerList.get(s_editOfferSpinner.getSelectedItemPosition()).getOfferID();

                for(Beacon beacon : beaconList)
                {
                    if(major == beacon.getMajor() && minor == beacon.getMinor())
                    {
                        updateBeacon(beacon.getBeaconID(),productID, offerID, major, minor);
                        break;
                    }
                }

            }
        }
    }

    private void insertBeacon(int productid, int offerid, int major, int minor) {
        ServerRequestHandler.uploadBeacon(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                // refresh our list of beacons
                try {
                    Log.e("error", jsonObject.getString("returnvalue"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getbeacons();
                hideSlide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error",volleyError.toString());
            }
        }, productid, offerid, major, minor, mContext);
    }

    private void updateBeacon(int beaconid,int productid, int offerid, int major, int minor) {
        ServerRequestHandler.editBeacon(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                // refresh our list of beacons
                try {
                    Log.e("error", jsonObject.getString("returnvalue"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getbeacons();
                hideSlide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", volleyError.toString());
            }
        }, beaconid, productid, offerid, major, minor, mContext);
    }

    private void deleteBeacon(final int beaconID,final int major,final int minor, final int position)
    {
        ServerRequestHandler.DeleteBeacon(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("returnvalue").equals("succes"))
                    {
                        DatabaseFunctions.getInstance(mContext).deleteBeaconLocation(Integer.toString(major),Integer.toString(minor));
                        ImageView d = screenBeaconList.get(position).getImageView();
                        d.setVisibility(View.GONE);
                        drawFrame.removeView(d);
                        drawFrame.invalidate();

                        getbeacons();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },Integer.toString(beaconID),mContext);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onBeaconInteraction(Uri uri);
    }

}
