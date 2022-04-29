package org.datapunch.starfish.resources.root;

import com.codahale.metrics.annotation.Timed;
import org.datapunch.starfish.api.emr.CreateClusterRequest;
import org.datapunch.starfish.api.emr.CreateClusterResponse;
import org.datapunch.starfish.api.emr.DeleteClusterResponse;
import org.datapunch.starfish.api.emr.GetClusterResponse;
import org.datapunch.starfish.core.EmrClusterConfiguration;
import org.datapunch.starfish.core.EmrClusterController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RootResource {
    public RootResource() {
    }

    @GET
    @Timed
    public String get() {
        return "OK";
    }
}
