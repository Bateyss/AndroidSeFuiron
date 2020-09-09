package com.fm.modules.app.carrito;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosPorMenuAdapter;
import com.fm.modules.adapters.RecyclerSubMenuAdapter;
import com.fm.modules.adapters.RecyclerSubMenuAdapter2;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.app.restaurantes.PlatillosActivity;
import com.fm.modules.entities.RespuestaOpcionSubMenuPorPlatillo;
import com.fm.modules.entities.RespuestaPlatilloPorMenu;
import com.fm.modules.entities.RespuestaSubMenuPorPlatillo;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionSubMenuService;
import com.fm.modules.service.PlatilloService;
import com.fm.modules.service.SubMenuService;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeleccionarComplementos extends AppCompatActivity {

    UnderThreads underThreads = new UnderThreads();
    UnderThreads2 underThreads2 = new UnderThreads2();
    RecyclerView rvComplementsArea;

    String idPlatillo;
    String nombre, descripcion, precio;
    NumberPicker numberPicker;
    AppCompatTextView tvFoodName, tvFoodDescripcion, tvFoodPrice;

    List<OpcionesDeSubMenu> opcionesDeSubMenusGlobal;
    List<SubMenu> subMenusGlobal;
    List<Platillo> platillosGlobal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_food_complements);

        rvComplementsArea = findViewById(R.id.rvComplementsArea);

        numberPicker = findViewById(R.id.npFoodQuantity);
        tvFoodName = findViewById(R.id.tvFoodName);
        tvFoodDescripcion = findViewById(R.id.tvFoodName);
        tvFoodPrice = findViewById(R.id.tvFoodPrice);

        opcionesDeSubMenusGlobal = new ArrayList<>();
        subMenusGlobal = new ArrayList<>();
        platillosGlobal = new ArrayList<>();

        /*idPlatillo = getIntent().getStringExtra("idPlatillo");
        nombre = getIntent().getStringExtra("nombre");
        descripcion = getIntent().getStringExtra("descripcion");
        precio = getIntent().getStringExtra("precio");*/

        tvFoodName.setText(GlobalRestaurantes.platillo.getNombre());
        tvFoodDescripcion.setText(GlobalRestaurantes.platillo.getDescripcion());
        tvFoodPrice.setText(String.valueOf(GlobalRestaurantes.platillo.getPrecioBase()));
        // FALTA IMG
        Toast.makeText(getApplicationContext(), "Platillo ID: " +idPlatillo, Toast.LENGTH_SHORT).show();

        underThreads.execute();
        //underThreads2.execute();

        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                double total;
                total = Double.parseDouble(String.valueOf(GlobalRestaurantes.platillo.getPrecioBase() * value));
                tvFoodPrice.setText(String.valueOf(total));
                Toast.makeText(getApplicationContext(), "Numero: " +value +" " +action.toString() +" \nPrecio: " +total, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class UnderThreads extends AsyncTask<String, String , List<OpcionesDeSubMenu>> {


        @Override
        protected List<OpcionesDeSubMenu> doInBackground(String... strings) {
            List<OpcionesDeSubMenu> opcionesDeSubMenus = new ArrayList<>();
            ///List<SubMenu> subMenuList = new ArrayList<>();

            try {
                //SubMenuService subMenuService = new SubMenuService();
                //subMenuList = subMenuService.obtenerSubMenusPorPlatillo(idPlatillo);
                OpcionSubMenuService opcionSubMenuService = new OpcionSubMenuService();
                opcionesDeSubMenus = opcionSubMenuService.obtenerOpcionSubMenus();

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return opcionesDeSubMenus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<OpcionesDeSubMenu> opcionesDeSubMenus) {
            super.onPostExecute(opcionesDeSubMenus);
            try {
                if (!opcionesDeSubMenus.isEmpty()){
                    //MenuItemViewAdapter adapter = new MenuItemViewAdapter(menu, RestauranteMenuActivity.this, R.layout.frg_restaurants);
                    //OpcionSubMenuService opcionSubMenuService = new OpcionSubMenuService();
                    //List<OpcionesDeSubMenu> opcionesDeSubMenuList = opcionSubMenuService.opcionesSubMenuPorPlatillo(idPlatillo);

                    opcionesDeSubMenusGlobal = opcionesDeSubMenus;

                    for(OpcionesDeSubMenu opciones: opcionesDeSubMenus){
                        if(!subMenusGlobal.contains(opciones.getSubMenu())){
                            subMenusGlobal.add(opciones.getSubMenu());
                        }
                    }

                    if(!subMenusGlobal.isEmpty()){
                        for(SubMenu subMenu: subMenusGlobal){
                            //if(!platillosGlobal.contains(subMenu.getPlatillo())){
                                platillosGlobal.add(subMenu.getPlatillo());
                            //}

                        }
                    }

                    for(Platillo platillo: platillosGlobal){
                        if(GlobalRestaurantes.platillo.getPlatilloId() == platillo.getPlatilloId()){
                            GlobalRestaurantes.platillo = platillo;
                        }
                    }


                    List<OpcionesDeSubMenu> opcionesSeleccionado = new ArrayList<>();
                    for(OpcionesDeSubMenu opcionesSubMenu: opcionesDeSubMenusGlobal){
                        if(opcionesSubMenu.getSubMenu().getPlatillo().getPlatilloId() == GlobalRestaurantes.platillo.getPlatilloId()){
                            opcionesSeleccionado.add(opcionesSubMenu);
                        }
                    }


                    System.out.println("Cantidad opcionesSelec: " +opcionesSeleccionado.size());

                    List<SubMenu> subMenusSeleccionado = new ArrayList<>();
                    for(OpcionesDeSubMenu opcionesSelec: opcionesSeleccionado){
                        //SubMenu suM = opcionesSelec.getSubMenu();
                        if(!subMenusSeleccionado.contains(opcionesSelec.getSubMenu())) {

                            if(!subMenusSeleccionado.equals(opcionesSelec.getSubMenu())){
                                subMenusSeleccionado.add(opcionesSelec.getSubMenu());
                            }

                        }
                        System.out.println(opcionesSelec);
                    }

                    List<SubMenu> noDuplicados = new ArrayList<>();
                    if(noDuplicados.isEmpty()){
                        for(int i=0; i < opcionesSeleccionado.size(); i++){
                            if(subMenusSeleccionado.get(i).getSubMenuId() != opcionesSeleccionado.get(i).getSubMenu().getSubMenuId()){
                                noDuplicados.add(opcionesSeleccionado.get(i).getSubMenu());
                            }
                        }
                    }




                    System.out.println("Cantidad SubMenuSelecionado: " +subMenusSeleccionado.size());
                    System.out.println("Cantidad SubMenuSelecionado2: " +noDuplicados.size());


                    RecyclerSubMenuAdapter2 adapter = new RecyclerSubMenuAdapter2(subMenusSeleccionado, opcionesSeleccionado, SeleccionarComplementos.this);
                    rvComplementsArea.setLayoutManager(new LinearLayoutManager(SeleccionarComplementos.this, LinearLayoutManager.VERTICAL, false));
                    rvComplementsArea.setAdapter(adapter);
                    Toast.makeText(SeleccionarComplementos.this, "SubMenus Cargados" +opcionesDeSubMenus.size(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(RestaurantesActivity.this, "Restaurantes No Cargados" +restaurantes.size(), Toast.LENGTH_SHORT).show();
                    //reiniciarAsynkProcess();
                }
            }catch (Throwable throwable){
                System.out.println("Error Activity: " +throwable.getMessage());
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public class UnderThreads2 extends AsyncTask<String, String , List<RespuestaOpcionSubMenuPorPlatillo>> {


        @Override
        protected List<RespuestaOpcionSubMenuPorPlatillo> doInBackground(String... strings) {
            List<RespuestaOpcionSubMenuPorPlatillo> opcionesDeSubMenus = new ArrayList<>();
            //List<RespuestaOpcionSubMenuPorPlatillo> subMenuList = new ArrayList<>();

            try {
                //SubMenuService subMenuService = new SubMenuService();
                //subMenuList = subMenuService.obtenerSubMenusPorPlatillo(idPlatillo);
                OpcionSubMenuService opcionSubMenuService = new OpcionSubMenuService();
                opcionesDeSubMenus = opcionSubMenuService.opcionesSubMenuPorPlatillo(String.valueOf(GlobalRestaurantes.platillo.getPlatilloId()));

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return opcionesDeSubMenus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<RespuestaOpcionSubMenuPorPlatillo> opcionesDeSubMenus) {
            super.onPostExecute(opcionesDeSubMenus);
            try {
                if (!opcionesDeSubMenus.isEmpty()){
                    //MenuItemViewAdapter adapter = new MenuItemViewAdapter(menu, RestauranteMenuActivity.this, R.layout.frg_restaurants);
                    //OpcionSubMenuService opcionSubMenuService = new OpcionSubMenuService();
                    //List<OpcionesDeSubMenu> opcionesDeSubMenuList = opcionSubMenuService.opcionesSubMenuPorPlatillo(idPlatillo);


                    RecyclerSubMenuAdapter adapter = new RecyclerSubMenuAdapter(opcionesDeSubMenus,SeleccionarComplementos.this);
                    rvComplementsArea.setLayoutManager(new LinearLayoutManager(SeleccionarComplementos.this, LinearLayoutManager.VERTICAL, false));
                    rvComplementsArea.setAdapter(adapter);
                    Toast.makeText(SeleccionarComplementos.this, "SubMenus Cargados" +opcionesDeSubMenus.size(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(RestaurantesActivity.this, "Restaurantes No Cargados" +restaurantes.size(), Toast.LENGTH_SHORT).show();
                    //reiniciarAsynkProcess();
                }
            }catch (Throwable throwable){
                System.out.println("Error Activity: " +throwable.getMessage());
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

}
