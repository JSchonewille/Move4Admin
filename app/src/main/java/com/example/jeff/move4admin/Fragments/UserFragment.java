package com.example.jeff.move4admin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jeff.move4admin.Library.DatabaseFunctions;
import com.example.jeff.move4admin.Library.LikesAdapter;
import com.example.jeff.move4admin.Library.User;
import com.example.jeff.move4admin.Library.UserLike;
import com.example.jeff.move4admin.R;
import com.example.jeff.move4admin.Library.UserAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String savedPath;
    ArrayList<User> userlist = new ArrayList<User>();
    ArrayList<UserLike> userlikes = new ArrayList<UserLike>();
    DatabaseFunctions dbf;
    private ImageView img;
    private TextView t_naam;
    private TextView t_mail;
    private TextView t_created;
    private TextView t_likesLabel;
    private GridView g_likes;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }
    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            ContextWrapper cw = new ContextWrapper(getActivity());
            savedPath = cw.getDir("imageDir", Context.MODE_PRIVATE).toString();
            dbf = DatabaseFunctions.getInstance(getActivity());
            userlist = dbf.getUsers();
            userlikes = dbf.getUserLikes();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_user, container, false);
        ListView l_users = (ListView) myInflatedView.findViewById(R.id.listView);
        img = (ImageView) myInflatedView.findViewById(R.id.imageView);
        t_naam = (TextView) myInflatedView.findViewById(R.id.t_nameLabel);
        t_mail = (TextView) myInflatedView.findViewById(R.id.t_mail);
        t_created = (TextView) myInflatedView.findViewById(R.id.t_created);
        t_likesLabel =( (TextView) myInflatedView.findViewById(R.id.t_likesLabel));
        g_likes = (GridView) myInflatedView.findViewById(R.id.g_likes);


        l_users.setAdapter(new UserAdapter(getActivity(),userlist));
        l_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               UserlistClick(adapterView,view,i,l);
            }
        });

        return myInflatedView;
    }



    public void UserlistClick(AdapterView<?> adapterView, View view, int Position, long id)
    {
        User u = userlist.get(Position);
        t_naam.setText(u.getName() + " " + u.getLastName());
        t_naam.setVisibility(View.VISIBLE);

        t_mail.setText   ("e-mail adress : " + u.getEmail());
        t_created.setText("aangemaakt    : " + u.getCreated());


        try {
            File f = new File(savedPath, u.getFilePath().substring(7));
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            b = Bitmap.createScaledBitmap(b, 800, 800, true);
            img.setImageBitmap(b);
        }
        catch (Exception e)
        {
            Bitmap noimg = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.nopic);
            noimg = Bitmap.createScaledBitmap(noimg, 800, 800, true);
            img.setImageBitmap(noimg);
        }

        for (UserLike ul :userlikes)
        {
            if(ul.getUserID() == u.getUserID())
            {
                ArrayList<String> list = ul.getLikes();

                if(list.size() >0) {
                    LikesAdapter l = new LikesAdapter(getActivity(),list);
                    g_likes.setAdapter(l);
                    t_likesLabel.setVisibility(View.VISIBLE);
                    g_likes.setVisibility(View.VISIBLE);
                }
                else{
                    t_likesLabel.setVisibility(View.INVISIBLE);
                    g_likes.setVisibility(View.INVISIBLE);
                }
                break;
            }
        }





    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onUserInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onUserInteraction(Uri uri);
    }

}
