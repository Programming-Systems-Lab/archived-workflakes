/**
 * AddressBookSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package AddressPackage.ws;

public class AddressBookSoapBindingStub extends org.apache.axis.client.Stub implements AddressPackage.ws.AddressBook {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    public AddressBookSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public AddressBookSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public AddressBookSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:AddressFetcher", "Address");
            cachedSerQNames.add(qName);
            cls = AddressPackage.ws.Address.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:AddressFetcher", "PhoneNumber");
            cachedSerQNames.add(qName);
            cls = AddressPackage.ws.PhoneNumber.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                if(_call.isPropertySupported(key))
                    _call.setProperty(key, super.cachedProperties.get(key));
                else
                    _call.setScopedProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public java.lang.String addEntry(java.lang.String in0, AddressPackage.ws.Address in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("urn:AddressFetcher", "Address"), AddressPackage.ws.Address.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "addEntry"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public AddressPackage.ws.Address getAddressFromName(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("urn:AddressFetcher", "Address"), AddressPackage.ws.Address.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "getAddressFromName"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (AddressPackage.ws.Address) _resp;
            } catch (java.lang.Exception _exception) {
                return (AddressPackage.ws.Address) org.apache.axis.utils.JavaUtils.convert(_resp, AddressPackage.ws.Address.class);
            }
        }
    }

    public java.util.Vector getAllListings() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"), java.util.Vector.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "getAllListings"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.util.Vector) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.util.Vector) org.apache.axis.utils.JavaUtils.convert(_resp, java.util.Vector.class);
            }
        }
    }

    public java.lang.String editName(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "editName"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String editAddress(java.lang.String in0, AddressPackage.ws.Address in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("urn:AddressFetcher", "Address"), AddressPackage.ws.Address.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "editAddress"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String editPhone(java.lang.String in0, AddressPackage.ws.PhoneNumber in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("urn:AddressFetcher", "PhoneNumber"), AddressPackage.ws.PhoneNumber.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "editPhone"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String editEmail(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "editEmail"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, in1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String removeAddress(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:AddressFetcher", "removeAddress"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

}
