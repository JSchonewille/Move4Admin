package com.example.jeff.move4admin.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4admin.Library.Beacon;
import com.example.jeff.move4admin.Library.BeaconDrawable;
import com.example.jeff.move4admin.Library.DatabaseFunctions;
import com.example.jeff.move4admin.Library.ServerRequestHandler;
import com.example.jeff.move4admin.Library.adapters.BeaconAdapter;
import com.example.jeff.move4admin.R;

import org.json.JSONArray;
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
    private static final String IMAGEVIEW_TAG = "The Android Logo";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Menu menu;
    private MenuItem m_add;
    private MenuItem m_save;
    private MenuItem m_edit;
    private MenuItem m_options;
    private Context mContext;
    private ArrayList<Beacon> beaconList = new ArrayList<Beacon>();
    private ArrayList<BeaconDrawable> screenBeaconList = new ArrayList<BeaconDrawable>();

    private ListView l_beaconListView;
    private ImageView i_star;
    private FrameLayout starFrame;
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
                return true;
            case R.id.action_save:
                return true;
            case R.id.action_edit:
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
                starFrame.setBackground(dr);
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
        m_add.setVisible(false);
        m_options.setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beacon, container, false);
        cw = new ContextWrapper(getActivity());
        i_star = (ImageView) v.findViewById(R.id.i_star);
        l_beaconListView = (ListView) v.findViewById(R.id.beaconList);
        starFrame = (FrameLayout) v.findViewById(R.id.f_starFrame);
        i_star.setTag(IMAGEVIEW_TAG);

        String beaconsFrameBackground = DatabaseFunctions.getInstance(mContext).getBeaconBackground();

        if (beaconsFrameBackground.length() > 5) {
            try {
                File f = new File(cw.getDir("imageDir", Context.MODE_PRIVATE).toString(), beaconsFrameBackground);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                BitmapDrawable dr = new BitmapDrawable(getResources(), b);
                starFrame.setBackground(dr);
            } catch (FileNotFoundException e) {
                Log.e("file not found", "could not find file " + beaconsFrameBackground);
            }
        }
        starFrame.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                return viewDrag(view, dragEvent);
            }
        });

        l_beaconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                beaconListClick(adapterView,view,i,l);
            }
        });

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

    private boolean viewClick(View view) {
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

        Drawable normalShape = i_star.getDrawable();
        Drawable targetShape = i_star.getDrawable();


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

    public void beaconListClick(AdapterView<?> adapterView, View view, int Position, long id)
    {
        BeaconDrawable bc = screenBeaconList.get(Position);
        ImageView iv = bc.getImageView();
        iv.setImageResource(android.R.drawable.star_big_on);


        int length = adapterView.getCount();
        for(int i = 0; i < length; i++)
        {
            if (adapterView.getChildAt(i).equals(view))
            {
                adapterView.getChildAt(i).setBackgroundResource(android.R.color.holo_blue_dark);
            }
            else
            {
                adapterView.getChildAt(i).setBackgroundResource(android.R.color.transparent);
            }
        }
        view.setBackgroundResource(android.R.color.holo_blue_light);
        for (BeaconDrawable bd : screenBeaconList)
        {
            if(bd.getImageView().equals(iv))
            {

            }
            else
            {
                bd.getImageView().setImageResource(android.R.drawable.star_big_off);
            }
        }
    }

    public void getbeacons() {
        screenBeaconList = DatabaseFunctions.getInstance(mContext).getBeaconLocations();

        ServerRequestHandler.getAllBeacons(new Response.Listener<JSONArray>() {
            float starx = 100;
            float stary = 100;

            @Override
            public void onResponse(JSONArray jsonArray) {
                beaconList.clear();
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


                        ImageView iv = new ImageView(mContext);


                        iv.setImageDrawable(i_star.getDrawable());
                        iv.setScaleType(i_star.getScaleType());
                        iv.setLayoutParams(i_star.getLayoutParams());
                        iv.setTag(iv);
                        iv.setY(stary);
                        iv.setX(starx);
                        iv.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                return viewClick(view);
                            }
                        });

                        boolean found = false;
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
                        starFrame.addView(iv);
                        starx += 50;
                        stary += 50;

                    } catch (Exception e) {
                        Log.e("error", e.toString());
                    }
                    // set adapter after filling our list of offers
                    if (l_beaconListView != null) {
                        l_beaconListView.setAdapter(new BeaconAdapter(mContext, beaconList));

//                        l_offerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                // function responsible for the clicks in the offer list
//                                offerListClick(adapterView, view, i, l);
//                                f_viewFrame.setVisibility(View.VISIBLE);
//                                f_editFrame.setVisibility(View.GONE);
//                                m_save.setVisible(false);
//                                m_add.setVisible(true);
//                            }
//                        });
//                        l_offerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                // function responsible for the product list long clicks
//                                offerListLongClick(adapterView, view, i, l);
//                                return true;
//                            }
//                        });
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mContext);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onBeaconInteraction(Uri uri);
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

}
