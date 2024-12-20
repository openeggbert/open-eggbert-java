package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
// WindowsPhoneSpeedyBlupi.Resource
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.internal;
//import com.openeggbert.jdotnet.System.CodeDom.Compiler.*;
//import com.openeggbert.jdotnet.System.ComponentModel.*;
import com.openeggbert.jdotnet.System.Diagnostics.*;
import com.openeggbert.jdotnet.System.Globalization.*;
import com.openeggbert.jdotnet.System.Resources.*;
//import com.openeggbert.jdotnet.System.Runtime.CompilerServices.*;
import static com.openeggbert.jdotnet.System.Type.typeof;
import com.openeggbert.jdotnet.System.object;

//[DebuggerNonUserCode]
//[GeneratedCode("System.Resources.Tools.StronglyTypedResourceBuilder", "4.0.0.0")]
//[CompilerGenerated]



    @internal class Resource
    {
        private static ResourceManager resourceMan;

        private static CultureInfo resourceCulture;

        //[EditorBrowsable(EditorBrowsableState.Advanced)]
        @internal static ResourceManager ResourceManager()
        {
            
            {
                if (object.ReferenceEquals(resourceMan, null))
                {
                    ResourceManager resourceManager = new ResourceManager("WindowsPhoneSpeedyBlupi.Resource", typeof(Resource.class).Assembly());
                    resourceMan = resourceManager;
                }
                return resourceMan;
            }
        }

        //[EditorBrowsable(EditorBrowsableState.Advanced)]
        @internal static CultureInfo Culture()
        {
            
            {
                return resourceCulture;
            }
        }
        @internal
        static void setCultureInfo(CultureInfo value) {
            resourceCulture = value;
        }

        @internal static String Title()
        {
            
            {
                return ResourceManager.GetString("Title", resourceCulture);
            }
        }

        @internal private Resource()
        {
        }
    }

